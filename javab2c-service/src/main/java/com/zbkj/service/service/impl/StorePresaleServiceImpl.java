package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.ProductConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.model.presale.StorePresale;
import com.zbkj.common.model.presale.StorePresaleUser;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.product.StoreProductAttr;
import com.zbkj.common.model.product.StoreProductAttrValue;
import com.zbkj.common.model.product.StoreProductDescription;
import com.zbkj.common.model.record.UserVisitRecord;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.service.dao.StorePresaleDao;
import com.zbkj.service.service.*;
import io.swagger.annotations.ApiModelProperty;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StorePresaleService 实现类
 */
@Service
public class StorePresaleServiceImpl extends ServiceImpl<StorePresaleDao, StorePresale> implements StorePresaleService {

    private static final ZoneId GMT = ZoneId.of("GMT+8");
    private static final Logger logger = LoggerFactory.getLogger(StorePresaleServiceImpl.class);

    @Resource
    private StorePresaleDao dao;

    @Autowired
    private StorePresaleUserService presaleUserService;


    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private StoreProductAttrService storeProductAttrService;

    @Autowired
    private StoreProductAttrValueService storeProductAttrValueService;

    @Autowired
    private StoreProductAttrResultService storeProductAttrResultService;

    @Autowired
    private StoreProductDescriptionService storeProductDescriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreProductRelationService storeProductRelationService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private UserVisitRecordService userVisitRecordService;


    /**
     * 分页显示预售商品表
     *
     * @param request          请求参数
     * @param pageParamRequest 分页类参数
     * @return List<StorePresale>
     */
    @Override
    public PageInfo<StorePresaleResponse> getList(StorePresaleSearchRequest request, PageParamRequest pageParamRequest) {
        Page<StorePresale> combinationPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StorePresale> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StorePresale::getIsDel, false);
        if (StrUtil.isNotEmpty(request.getKeywords())) {
            lambdaQueryWrapper.and(i -> i.like(StorePresale::getProductId, request.getKeywords())
                    .or().like(StorePresale::getId, request.getKeywords())
                    .or().like(StorePresale::getTitle, request.getKeywords()));
        }
        if (ObjectUtil.isNotNull(request.getIsShow())) {
            lambdaQueryWrapper.eq(StorePresale::getIsShow, request.getIsShow() == 1);
        }

        lambdaQueryWrapper.orderByDesc(StorePresale::getSort, StorePresale::getId);
        List<StorePresale> storePresaleList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(storePresaleList)) {
            return CommonPage.copyPageInfo(combinationPage, CollUtil.newArrayList());
        }

        List<StorePresaleResponse> responseList = storePresaleList.stream().map(combination -> {
            //原价 预约人数 中签人数  限量剩余
            StorePresaleResponse combinationResponse = new StorePresaleResponse();
            BeanUtils.copyProperties(combination, combinationResponse);
            combinationResponse.setRemainingQuota(combination.getQuota());
            List<StorePresaleUser> pinkList = presaleUserService.getListByCid(combination.getId());

            combinationResponse.setWinnerPeople(0);
            if (CollUtil.isNotEmpty(pinkList)) {
                List<StorePresaleUser> successTeam = pinkList.stream().filter(i -> i.getIsWinner() == 2).collect(Collectors.toList());
                combinationResponse.setWinnerPeople(successTeam.size());
            }
            combinationResponse.setStopTimeStr(DateUtil.timestamp2DateStr(combination.getStopTime(), Constants.DATE_FORMAT));


            return combinationResponse;
        }).collect(Collectors.toList());

        return CommonPage.copyPageInfo(combinationPage, responseList);
    }

    /**
     * 新增预售商品
     *
     * @param request 新增请求参数
     * @return Boolean
     */
    @Override
    public Integer saveCombination(StorePresaleRequest request) {
        if (!request.getSpecType()) {
            if (request.getAttrValue().size() > 1) {
                throw new CrmebException("单规格商品属性值不能大于1");
            }
        }
        // 过滤掉checked=false的数据
        //        clearNotCheckedAndValidationPrice(request);
        long startTime = (long) DateUtil.getSecondTimestamp(request.getStartTime()) * 1000;
        long stopTime = (long) DateUtil.getSecondTimestamp(request.getStopTime()) * 1000;
        long prizeTime = (long) DateUtil.getSecondTimestamp(request.getPrizeTime()) * 1000;
        long payStopTime = (long) DateUtil.getSecondTimestamp(request.getPayStopTime()) * 1000;
        if (stopTime < startTime) {
            throw new CrmebException("预约开始时间不能小于预约结束时间");
        }
        if (prizeTime < stopTime) {
            throw new CrmebException("开奖时间不能小于预约结束时间");
        }
        if (payStopTime < prizeTime) {
            throw new CrmebException("付款结束时间不能小于开奖时间");
        }

        StorePresale storePresale = new StorePresale();
        BeanUtils.copyProperties(request, storePresale);

        storePresale.setStartTime(startTime);
        storePresale.setStopTime(stopTime);
        storePresale.setPrizeTime(prizeTime);
        storePresale.setPayStopTime(payStopTime);

        storePresale.setId(null);
        // 头图、轮播图
        storePresale.setImage(systemAttachmentService.clearPrefix(request.getImage()));
        storePresale.setImages(systemAttachmentService.clearPrefix(request.getImages()));
        // 预售分类
        storePresale.setPresaleCateId(request.getPresaleCateId());

        storePresale.setSales(0);

        List<StoreProductAttrValueAddRequest> attrValueAddRequestList = request.getAttrValue();
        // 计算价格
        StoreProductAttrValueAddRequest minAttrValue = attrValueAddRequestList.stream().min(Comparator.comparing(StoreProductAttrValueAddRequest::getPrice)).get();
        storePresale.setPrice(minAttrValue.getPrice());
        storePresale.setOtPrice(minAttrValue.getOtPrice());
        storePresale.setCost(minAttrValue.getCost());
        storePresale.setStock(attrValueAddRequestList.stream().mapToInt(StoreProductAttrValueAddRequest::getStock).sum());
        int quotaTotal = attrValueAddRequestList.stream().mapToInt(StoreProductAttrValueAddRequest::getQuota).sum();
        storePresale.setQuota(quotaTotal);
        storePresale.setQuotaShow(quotaTotal);

        List<StoreProductAttrAddRequest> addRequestList = request.getAttr();
        List<StoreProductAttr> attrList = addRequestList.stream().map(e -> {
            StoreProductAttr attr = new StoreProductAttr();
            BeanUtils.copyProperties(e, attr);
            attr.setType(Constants.PRODUCT_TYPE_PRESALE);
            return attr;
        }).collect(Collectors.toList());

        List<StoreProductAttrValue> attrValueList = attrValueAddRequestList.stream().map(e -> {
            StoreProductAttrValue attrValue = new StoreProductAttrValue();
            BeanUtils.copyProperties(e, attrValue);
            attrValue.setSuk(e.getSuk());
            attrValue.setQuota(e.getQuota());
            attrValue.setQuotaShow(e.getQuota());
            attrValue.setType(Constants.PRODUCT_TYPE_PRESALE);
            attrValue.setImage(systemAttachmentService.clearPrefix(e.getImage()));
            return attrValue;
        }).collect(Collectors.toList());

        // 处理富文本
        StoreProductDescription spd = new StoreProductDescription();
        spd.setDescription(request.getContent().length() > 0 ? systemAttachmentService.clearPrefix(request.getContent()) : "");
        spd.setType(Constants.PRODUCT_TYPE_PRESALE);

        Integer storePresaleId = transactionTemplate.execute(e -> {
            save(storePresale);

            attrList.forEach(attr -> attr.setProductId(storePresale.getId()));
            attrValueList.forEach(value -> value.setProductId(storePresale.getId()));
            storeProductAttrService.saveBatch(attrList);
            storeProductAttrValueService.saveBatch(attrValueList);

            spd.setProductId(storePresale.getId());
            storeProductDescriptionService.deleteByProductId(storePresale.getId(), Constants.PRODUCT_TYPE_PRESALE);
            storeProductDescriptionService.save(spd);

            return storePresale.getId();
        });

        return storePresaleId;
    }

    /**
     * 删除预售商品
     */
    @Override
    public Boolean deleteById(Integer id) {
        StorePresale combination = getById(id);
        long timeMillis = System.currentTimeMillis();
        if (combination.getIsShow().equals(true) && combination.getStartTime() <= timeMillis && timeMillis <= combination.getStopTime()) {
            throw new CrmebException("活动开启中，商品不支持删除");
        }

        StorePresale storePresale = new StorePresale();
        storePresale.setId(id).setIsDel(true);
        return updateById(storePresale);
    }

    /**
     * 编辑预售商品
     *
     * @param request 编辑请求参数
     * @return Boolean
     */
    @Override
    public Boolean updateCombination(StorePresaleRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new CrmebException("预售商品id不能为空");
        }
        StorePresale existCombination = getById(request.getId());
        if (ObjectUtil.isNull(existCombination) || existCombination.getIsDel()) {
            throw new CrmebException("预售商品不存在");
        }
        long timeMillis = System.currentTimeMillis();
        if (existCombination.getIsShow().equals(true) && existCombination.getStartTime() <= timeMillis && timeMillis <= existCombination.getStopTime()) {
            throw new CrmebException("活动开启中，商品不支持修改");
        }
        long startTime = (long) DateUtil.getSecondTimestamp(request.getStartTime()) * 1000;
        long stopTime = (long) DateUtil.getSecondTimestamp(request.getStopTime()) * 1000;
        long prizeTime = (long) DateUtil.getSecondTimestamp(request.getPrizeTime()) * 1000;
        long payStopTime = (long) DateUtil.getSecondTimestamp(request.getPayStopTime()) * 1000;
        if (stopTime < startTime) {
            throw new CrmebException("预约开始时间不能小于预约结束时间");
        }
        if (prizeTime < stopTime) {
            throw new CrmebException("开奖时间不能小于预约结束时间");
        }
        if (payStopTime < prizeTime) {
            throw new CrmebException("付款结束时间不能小于开奖时间");
        }


        StorePresale storePresale = new StorePresale();
        BeanUtils.copyProperties(request, storePresale);
        // 头图、轮播图
        storePresale.setImage(systemAttachmentService.clearPrefix(request.getImage()));
        storePresale.setImages(systemAttachmentService.clearPrefix(request.getImages()));
        // 预售分类
        storePresale.setPresaleCateId(request.getPresaleCateId());

        storePresale.setStartTime(startTime);
        storePresale.setStopTime(stopTime);
        storePresale.setPrizeTime(prizeTime);
        storePresale.setPayStopTime(payStopTime);

        List<StoreProductAttrValueAddRequest> attrValueAddRequestList = request.getAttrValue();
        // 计算价格
        StoreProductAttrValueAddRequest minAttrValue = attrValueAddRequestList.stream().min(Comparator.comparing(StoreProductAttrValueAddRequest::getPrice)).get();
        storePresale.setPrice(minAttrValue.getPrice());
        storePresale.setOtPrice(minAttrValue.getOtPrice());
        int quotaTotal = attrValueAddRequestList.stream().mapToInt(StoreProductAttrValueAddRequest::getQuota).sum();
        storePresale.setQuota(quotaTotal);
        storePresale.setQuotaShow(quotaTotal);

        // attr部分
        List<StoreProductAttrAddRequest> addRequestList = request.getAttr();
        List<StoreProductAttr> attrAddList = CollUtil.newArrayList();
        List<StoreProductAttr> attrUpdateList = CollUtil.newArrayList();
        addRequestList.forEach(e -> {
            StoreProductAttr attr = new StoreProductAttr();
            BeanUtils.copyProperties(e, attr);
            if (ObjectUtil.isNull(attr.getId())) {
                attr.setProductId(storePresale.getId());
                attr.setType(Constants.PRODUCT_TYPE_PRESALE);
                attrAddList.add(attr);
            } else {
                attr.setProductId(storePresale.getId());
                attr.setIsDel(false);
                attrUpdateList.add(attr);
            }
        });

        // attrValue部分
        List<StoreProductAttrValue> attrValueAddList = CollUtil.newArrayList();
        List<StoreProductAttrValue> attrValueUpdateList = CollUtil.newArrayList();
        attrValueAddRequestList.forEach(e -> {
            e.setAttrValue(StringEscapeUtils.unescapeJavaScript(e.getAttrValue()));
            StoreProductAttrValue attrValue = new StoreProductAttrValue();
            BeanUtils.copyProperties(e, attrValue);
            attrValue.setSuk(e.getSuk());
            attrValue.setImage(systemAttachmentService.clearPrefix(e.getImage()));
            attrValue.setQuota(e.getQuota());
            attrValue.setQuotaShow(e.getQuota());
            if (ObjectUtil.isNull(attrValue.getId())) {
                attrValue.setProductId(storePresale.getId());
                attrValue.setType(Constants.PRODUCT_TYPE_PRESALE);
                attrValueAddList.add(attrValue);
            } else {
                attrValue.setProductId(storePresale.getId());
                attrValue.setIsDel(false);
                attrValueUpdateList.add(attrValue);
            }
        });

        // 处理富文本
        StoreProductDescription spd = new StoreProductDescription();
        spd.setDescription(request.getContent().length() > 0 ? systemAttachmentService.clearPrefix(request.getContent()) : "");
        spd.setType(Constants.PRODUCT_TYPE_PRESALE);
        spd.setProductId(request.getId());

        Boolean execute = transactionTemplate.execute(e -> {
            dao.updateById(storePresale);

            // 先删除原用attr+value
            storeProductAttrService.deleteByProductIdAndType(storePresale.getId(), Constants.PRODUCT_TYPE_PRESALE);
            storeProductAttrValueService.deleteByProductIdAndType(storePresale.getId(), Constants.PRODUCT_TYPE_PRESALE);

            if (CollUtil.isNotEmpty(attrAddList)) {
                storeProductAttrService.saveBatch(attrAddList);
            }
            if (CollUtil.isNotEmpty(attrUpdateList)) {
                storeProductAttrService.saveOrUpdateBatch(attrUpdateList);
            }

            if (CollUtil.isNotEmpty(attrValueAddList)) {
                storeProductAttrValueService.saveBatch(attrValueAddList);
            }
            if (CollUtil.isNotEmpty(attrValueUpdateList)) {
                storeProductAttrValueService.saveOrUpdateBatch(attrValueUpdateList);
            }

            storeProductDescriptionService.deleteByProductId(storePresale.getId(), Constants.PRODUCT_TYPE_PRESALE);
            storeProductDescriptionService.save(spd);

            return Boolean.TRUE;
        });

        return execute;
    }

    /**
     * 预售商品详情
     *
     * @param id 预售商品ID
     * @return StoreProductInfoResponse
     */
    @Override
    public StoreProductInfoResponse getAdminDetail(Integer id) {
        StorePresale storePresale = dao.selectById(id);
        if (ObjectUtil.isNull(storePresale) || storePresale.getIsDel()) {
            throw new CrmebException("未找到对应商品信息");
        }
        StoreProductInfoResponse storeProductResponse = new StoreProductInfoResponse();
        BeanUtils.copyProperties(storePresale, storeProductResponse);

        // 查询attr
        List<StoreProductAttr> attrs = storeProductAttrService.getListByProductIdAndType(id, Constants.PRODUCT_TYPE_PRESALE);
        storeProductResponse.setAttr(attrs);
        storeProductResponse.setSliderImage(String.join(",", storePresale.getImages()));

        boolean specType = false;
        if (attrs.size() > 1) {
            specType = true;
        }
        storeProductResponse.setSpecType(specType);

        List<StoreProductAttrValue> comAttrValueList = storeProductAttrValueService.getListByProductIdAndType(id, ProductConstants.PRODUCT_TYPE_PRESALE);
        // 查询主商品sku
        List<StoreProductAttrValue> attrValueList = storeProductAttrValueService.getListByProductIdAndType(storePresale.getProductId(), Constants.PRODUCT_TYPE_NORMAL);

        List<AttrValueResponse> valueResponseList = attrValueList.stream().map(e -> {
            AttrValueResponse valueResponse = new AttrValueResponse();
            Integer tempId = 0;
            for (StoreProductAttrValue value : comAttrValueList) {
                if (value.getSuk().equals(e.getSuk())) {
                    tempId = value.getId();
                    BeanUtils.copyProperties(value, valueResponse);
                    break;
                }
            }
            if (tempId.equals(0)) {
                BeanUtils.copyProperties(e, valueResponse);
                valueResponse.setId(null);
            } else {
                valueResponse.setId(tempId);
            }
            return valueResponse;
        }).collect(Collectors.toList());
        storeProductResponse.setAttrValue(valueResponseList);

        StoreProductDescription sd = storeProductDescriptionService.getByProductIdAndType(id, Constants.PRODUCT_TYPE_PRESALE);
        if (ObjectUtil.isNotNull(sd)) {
            storeProductResponse.setContent(ObjectUtil.isNull(sd.getDescription()) ? "" : sd.getDescription());
        }
        return storeProductResponse;
    }

    /**
     * 修改预售商品状态
     */
    @Override
    public Boolean updateCombinationShow(Integer id, Boolean isShow) {
        StorePresale temp = getById(id);
        if (ObjectUtil.isNull(temp) || temp.getIsDel()) {
            throw new CrmebException("预售商品不存在");
        }
        if (isShow) {
            // 判断商品是否存在
            StoreProduct product = storeProductService.getById(temp.getProductId());
            if (ObjectUtil.isNull(product)) {
                throw new CrmebException("关联的商品已删除，无法开启活动");
            }
        }

        StorePresale storePresale = new StorePresale();
        storePresale.setId(id).setIsShow(isShow);
        return updateById(storePresale);
    }

    /**
     * 预售商品抽签
     * @return
     */
    @Override
    public String presaleGoodsWinner() {
        LambdaQueryWrapper<StorePresale> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StorePresale::getIsDel, false);
        long millis = System.currentTimeMillis();
        lambdaQueryWrapper.le(StorePresale::getPrizeTime,millis);
        lambdaQueryWrapper.ge(StorePresale::getPayStopTime, millis);

        List<StorePresale> storePresaleList = dao.selectList(lambdaQueryWrapper);
        if(storePresaleList.size() == 0){
            return "0";
        }
        for(StorePresale storePresale : storePresaleList){
            Integer quota = storePresale.getQuota();
            //待开奖用户数量
            List<StorePresaleUser> presaleUsers = presaleUserService.getUserByPresaleIdAndIsWinner(storePresale.getId(),0);
            if(presaleUsers.size() == 0){
                continue;
            }
            System.out.println(presaleUsers.size() +" 数量 "+ quota);
            if(presaleUsers.size() > quota){
//                Random rd=new Random();
//                int[] array=new int [quota];
//                for(int i=0;i<array.length;i++){
//                    array[i]=rd.nextInt(presaleUsers.size());
//                }
//                Arrays.sort(array);
//                List<Integer> numlist=new ArrayList<Integer>();
//                for(int i:array){
//                    numlist.add(i);
//                }
//                Set<Integer>numSet=new HashSet<Integer>();
//                numSet.addAll(numlist);
//                Object[]array2=numSet.toArray();
//                Arrays.sort(array2);
//                for(int j=0;j<array2.length;j++) {
//                    StorePresaleUser storePresaleUser  = presaleUsers.get((int)array2[j]);
//                    storePresaleUser.setIsWinner(2);
//                    storePresaleUser.setWinnerTime(System.currentTimeMillis());
//                    presaleUserService.updateById(storePresaleUser);
//                }

                List<Integer> numlist=new ArrayList<Integer>();
                Random rd=new Random();
                for(int i = 0; i< quota; i++){
                    Integer id = rd.nextInt(presaleUsers.size());
                    if(!numlist.contains(id)){
                        numlist.add(id);
                    }else{
                        i--;
                    }
                }
                for(Integer index : numlist) {
                    StorePresaleUser storePresaleUser  = presaleUsers.get(index);
                    storePresaleUser.setIsWinner(2);
                    storePresaleUser.setWinnerTime(System.currentTimeMillis());
                    presaleUserService.updateById(storePresaleUser);
                }

                //设置未中签用户
                List<StorePresaleUser> presaleUsersTwo = presaleUserService.getUserByPresaleIdAndIsWinner(storePresale.getId(),0);
                for(StorePresaleUser storePresaleUser : presaleUsersTwo) {
                    storePresaleUser.setIsWinner(1);
                    presaleUserService.updateById(storePresaleUser);
                }

            }else{
                for(StorePresaleUser storePresaleUser : presaleUsers) {
                    storePresaleUser.setIsWinner(2);
                    storePresaleUser.setWinnerTime(System.currentTimeMillis());
                    presaleUserService.updateById(storePresaleUser);
                }
            }

//            //待开奖用户数量
//            Integer winnerNumber = presaleUserService.getUserByPresaleIdAndIsWinner(storePresale.getId(),3).size();
//            //剩余中签名额
//            Integer surplus = quota - winnerNumber;
//            if(surplus <= 0){
//                continue;
//            }
//            List<StorePresaleUser> presaleUsers = presaleUserService.getUserByPresaleIdAndIsWinner(storePresale.getId(),0);
//            if(presaleUsers.size() != 0){
//
//                List<Integer> winners = new ArrayList<>();
//                if(presaleUsers.size() > 3){
//                    for(int i = 0; i < 3; i++){
//                        int winner = (int)(Math.random() * presaleUsers.size());
//                        winners.add(winner);
//                    }
//                }else{
//                    int winner = (int)(Math.random() * presaleUsers.size());
//                    winners.add(winner);
//                }
//                for(int i = 0; i < winners.size(); i++){
//                    if(surplus <= 0){
//                        break;
//                    }
//                    int randomIndex = winners.get(i);
//                    StorePresaleUser presaleUser = presaleUsers.get(randomIndex);
//                    presaleUser.setIsWinner(3);
//                    presaleUser.setWinnerTime(System.currentTimeMillis());
//                    presaleUserService.updateById(presaleUser);
//                    System.out.println("中签用户：" + presaleUser);
//                    surplus --;
//                    System.out.println("剩余名额：" + surplus);
//                }
//            }
        }
        return "";
    }


    /**
     * admin预售统计
     */
//    @Override
//    public Map<String, Object> getAdminStatistics() {
//        StorePink spavPink = new StorePink();
//        spavPink.setKId(0);
//        List<StorePink> pinkList = storePinkService.getByEntity(spavPink);
//        Map<String, Object> map = new HashMap<>();
//        map.put("countPeople", 0);
//        map.put("countTeam", 0);
//        if (CollUtil.isNotEmpty(pinkList)) {
//            map.put("countPeople", storePinkService.count());
//            long countTeam = pinkList.stream().filter(i -> i.getStatus() == 2).count();
//            map.put("countTeam", countTeam);
//        }
//        return map;
//    }

    /**
     * H5预售商品列表
     */
    @Override
    public List<StorePresaleH5Response> getH5List(ProductRequest request, PageParamRequest pageParamRequest) {
        LambdaQueryWrapper<StorePresale> lqw = Wrappers.lambdaQuery();
        lqw.select(StorePresale::getId, StorePresale::getSales, StorePresale::getProductId, StorePresale::getImage, StorePresale::getTitle
                , StorePresale::getTitleEn, StorePresale::getTitleMy, StorePresale::getPeople, StorePresale::getQuota, StorePresale::getStartTime,StorePresale::getAddTime, StorePresale::getAddTime, StorePresale::getPrice,StorePresale::getOtPrice);

        lqw.eq(StorePresale::getIsDel, false);
        lqw.eq(StorePresale::getIsShow, true);
        long millis = System.currentTimeMillis();
        lqw.ge(StorePresale::getPayStopTime, millis);
        // 分类查询
        if (ObjectUtil.isNotNull(request.getCid()) && request.getCid() > 0) {
            //查找当前类下的所有子类
            List<Category> childVoListByPid = categoryService.getChildVoListByPid(request.getCid());
            List<Integer> categoryIdList = childVoListByPid.stream().map(Category::getId).collect(Collectors.toList());
            categoryIdList.add(request.getCid());
            lqw.apply(CrmebUtil.getFindInSetSql("presale_cate_id", (ArrayList<Integer>) categoryIdList));
        }
        if (StrUtil.isNotBlank(request.getKeyword())) {
            if (CrmebUtil.isString2Num(request.getKeyword())) {
                Integer productId = Integer.valueOf(request.getKeyword());
                lqw.like(StorePresale::getId, productId);
            } else {
                lqw.like(StorePresale::getTitle, request.getKeyword());
            }
        }
        // 排序部分
        if (StrUtil.isNotBlank(request.getSalesOrder())) {
            if (request.getSalesOrder().equals(Constants.SORT_DESC)) {
                lqw.last(" order by sales  desc, sort desc, id desc");
            } else {
                lqw.last(" order by sales asc, sort asc, id asc");
            }
        } else {
            if (StrUtil.isNotBlank(request.getPriceOrder())) {
                if (request.getPriceOrder().equals(Constants.SORT_DESC)) {
                    lqw.orderByDesc(StorePresale::getPrice);
                } else {
                    lqw.orderByAsc(StorePresale::getPrice);
                }
            }

            lqw.orderByDesc(StorePresale::getSort);
            lqw.orderByDesc(StorePresale::getId);
        }

        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<StorePresale> combinationList = dao.selectList(lqw);
        if (CollUtil.isEmpty(combinationList)) {
            return CollUtil.newArrayList();
        }
        return combinationList.stream().map(e -> {
            StorePresaleH5Response response = new StorePresaleH5Response();
            BeanUtils.copyProperties(e, response);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = formatter.format(e.getStartTime());
            response.setStartTimeStr(strTime);
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * H5预售商品详情
     *
     * @param comId 预售商品编号
     * @return CombinationDetailResponse
     */
    @Override
    public PresaleDetailResponse getH5Detail(Integer comId) {
        PresaleDetailResponse detailResponse = new PresaleDetailResponse();
        StorePresale storePresale = getById(comId);
        if (ObjectUtil.isNull(storePresale) || storePresale.getIsDel()) {
            throw new CrmebException("对应预售商品不存在");
        }
        if (!storePresale.getIsShow()) {
            throw new CrmebException("预售商品已下架");
        }
        PresaleDetailH5Response infoResponse = new PresaleDetailH5Response();
        BeanUtils.copyProperties(storePresale, infoResponse);
        infoResponse.setStoreName(storePresale.getTitle());
        infoResponse.setStoreNameEn(storePresale.getTitleEn());
        infoResponse.setStoreNameMy(storePresale.getTitleMy());
        infoResponse.setSliderImage(storePresale.getImages());
        infoResponse.setStoreInfo(storePresale.getInfo());
        // 详情
        StoreProductDescription sd = storeProductDescriptionService.getOne(
                new LambdaQueryWrapper<StoreProductDescription>()
                        .eq(StoreProductDescription::getProductId, comId)
                        .eq(StoreProductDescription::getType, Constants.PRODUCT_TYPE_PRESALE));
        if (ObjectUtil.isNotNull(sd)) {
            infoResponse.setContent(ObjectUtil.isNull(sd.getDescription()) ? "" : sd.getDescription());
        }

        // 获取主商品信息
        StoreProduct storeProduct = storeProductService.getById(storePresale.getProductId());
        // 主商品状态
        if (storeProduct.getIsDel()) {
            detailResponse.setMasterStatus("delete");
        } else if (!storeProduct.getIsShow()) {
            detailResponse.setMasterStatus("soldOut");
        } else if (storeProduct.getStock() <= 0) {
            detailResponse.setMasterStatus("sellOut");
        } else {
            detailResponse.setMasterStatus("normal");
        }

        // 预售销量 = 原商品销量（包含虚拟销量）
        infoResponse.setSales(storeProduct.getSales());
        infoResponse.setFicti(storeProduct.getFicti());
        // 预售结束时间
        infoResponse.setStopTime(storePresale.getStopTime());

        detailResponse.setStorePresale(infoResponse);

        // 获取预售商品规格
        List<StoreProductAttr> attrList = storeProductAttrService.getListByProductIdAndType(comId, Constants.PRODUCT_TYPE_PRESALE);
        // 根据制式设置attr属性
//        List<ProductAttrResponse> skuAttr = getSkuAttr(attrList);
        detailResponse.setProductAttr(attrList);

        // 根据制式设置sku属性
        HashMap<String, Object> skuMap = new HashMap<>();
        // 获取主商品sku
        List<StoreProductAttrValue> storeProductAttrValues = storeProductAttrValueService.getListByProductIdAndType(storePresale.getProductId(), Constants.PRODUCT_TYPE_NORMAL);
        // 获取预售商品sku
        List<StoreProductAttrValue> combinationAttrValues = storeProductAttrValueService.getListByProductIdAndType(storePresale.getId(), Constants.PRODUCT_TYPE_PRESALE);

        for (StoreProductAttrValue productAttrValue : storeProductAttrValues) {
            StoreProductAttrValueResponse atr = new StoreProductAttrValueResponse();
            List<StoreProductAttrValue> valueList = combinationAttrValues.stream().filter(e -> productAttrValue.getSuk().equals(e.getSuk())).collect(Collectors.toList());
            if (CollUtil.isEmpty(valueList)) {
                BeanUtils.copyProperties(productAttrValue, atr);
            } else {
                BeanUtils.copyProperties(valueList.get(0), atr);
            }
            if (ObjectUtil.isNull(atr.getQuota())) {
                atr.setQuota(0);
            }
            skuMap.put(atr.getSuk(), atr);
        }
        detailResponse.setProductValue(skuMap);

        // 设置点赞和收藏
//        User user = userService.getInfo();
//        if (ObjectUtil.isNotNull(user) && ObjectUtil.isNotNull(user.getUid())) {
//            detailResponse.setUserCollect(storeProductRelationService.getLikeOrCollectByUser(user.getUid(), storePresale.getProductId(), false).size() > 0);
//        } else {
//            detailResponse.setUserCollect(false);
//        }

        // 保存用户访问记录
        UserVisitRecord visitRecord = new UserVisitRecord();
        visitRecord.setDate(cn.hutool.core.date.DateUtil.date().toString("yyyy-MM-dd"));
        visitRecord.setUid(userService.getUserId());
        visitRecord.setVisitType(3);
        userVisitRecordService.save(visitRecord);
        return detailResponse;
    }

    /**
     * 去预约
     *
     * @param presaleId 预售商品ID
     * @return GoPinkResponse
     */
    @Override
    public Boolean goPresale(Integer presaleId) {
        StorePresale storePresale = getByIdException(presaleId);

        long timeMillis = System.currentTimeMillis();
        if(timeMillis >= storePresale.getStopTime()){
            throw new CrmebException("商品预约已结束");
        }

        List<StorePresaleUser>  storePresaleUsers = presaleUserService.getPresaleByPresaleId(presaleId);
        if(storePresaleUsers.size() >= 1){
            throw new CrmebException("您已经预约过了");
        }

        StorePresaleUser storePresaleUser = new StorePresaleUser();
        User user = userService.getInfo();
        storePresaleUser.setUid(user.getUid());
        storePresaleUser.setAvatar(user.getAvatar());
        storePresaleUser.setNickname(user.getNickname());

        storePresaleUser.setPid(storePresale.getProductId());

        storePresaleUser.setImage(storePresale.getImage());
        storePresaleUser.setTitle(storePresale.getTitle());
        storePresaleUser.setAddTime(timeMillis);
        storePresaleUser.setIsWinner(0);
        storePresaleUser.setPresaleId(presaleId);
        storePresaleUser.setIsSystem(false);
        presaleUserService.save(storePresaleUser);
        storePresale.setSubscribe((int)storePresale.getSubscribe() + 1);
        updateById(storePresale);
        return true;
    }

    /**
     * 更多预售信息
     */
    @Override
    public PageInfo<StorePresale> getMore(PageParamRequest pageParamRequest, Integer comId) {
        Page<StorePresale> combinationPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StorePresale> lqw = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(comId)) {
            lqw.ne(StorePresale::getId, comId);
        }
        lqw.eq(StorePresale::getIsDel, false);
        lqw.eq(StorePresale::getIsShow, true);
        long millis = System.currentTimeMillis();
        lqw.le(StorePresale::getStartTime, millis);
        lqw.ge(StorePresale::getStopTime, millis);
        lqw.orderByDesc(StorePresale::getSort, StorePresale::getId);
        List<StorePresale> storePresales = dao.selectList(lqw);
        return CommonPage.copyPageInfo(combinationPage, storePresales);
    }

    @Override
    public void consumeProductStock() {
        String redisKey = Constants.PRODUCT_COMBINATION_STOCK_UPDATE;
        Long size = redisUtil.getListSize(redisKey);
        logger.info("StoreProductServiceImpl.doProductStock | size:" + size);
        if (size < 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            //如果10秒钟拿不到一个数据，那么退出循环
            Object data = redisUtil.getRightPop(redisKey, 10L);
            if (null == data) {
                continue;
            }
            try {
                StoreProductStockRequest storeProductStockRequest =
                        com.alibaba.fastjson.JSONObject.toJavaObject(com.alibaba.fastjson.JSONObject.parseObject(data.toString()), StoreProductStockRequest.class);
                boolean result = doProductStock(storeProductStockRequest);
                if (!result) {
                    redisUtil.lPush(redisKey, data);
                }
            } catch (Exception e) {
                redisUtil.lPush(redisKey, data);
            }
        }
    }

    /**
     * 获取当前时间的预售商品
     *
     * @param productId 商品编号
     */
    @Override
    public List<StorePresale> getCurrentBargainByProductId(Integer productId) {
        LambdaQueryWrapper<StorePresale> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresale::getProductId, productId);
        lqw.eq(StorePresale::getIsShow, true);
        long millis = System.currentTimeMillis();
        lqw.le(StorePresale::getStartTime, millis);
        lqw.ge(StorePresale::getStopTime, millis);
        lqw.orderByDesc(StorePresale::getSort, StorePresale::getId);
        return dao.selectList(lqw);
    }

    /**
     * 商品是否存在预售活动
     *
     * @param productId 商品编号
     */
    @Override
    public Boolean isExistActivity(Integer productId) {
        LambdaQueryWrapper<StorePresale> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresale::getProductId, productId);
        List<StorePresale> combinationList = dao.selectList(lqw);
        if (CollUtil.isEmpty(combinationList)) {
            return false;
        }
        // 判断关联的商品是否处于活动开启状态
        List<StorePresale> list = combinationList.stream().filter(i -> i.getIsShow().equals(true)).collect(Collectors.toList());
        return CollUtil.isNotEmpty(list);
    }

    /**
     * 查询带异常
     *
     * @param presaleId 预售商品id
     * @return StorePresale
     */
    @Override
    public StorePresale getByIdException(Integer presaleId) {
        LambdaQueryWrapper<StorePresale> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresale::getId, presaleId);
        lqw.eq(StorePresale::getIsDel, false);
        lqw.eq(StorePresale::getIsShow, true);
        StorePresale storePresale = dao.selectOne(lqw);
        if (ObjectUtil.isNull(storePresale)) throw new CrmebException("预售商品不存在或未开启");
        return storePresale;
    }

    /**
     * 添加/扣减库存
     *
     * @param id   秒杀商品id
     * @param num  数量
     * @param type 类型：add—添加，sub—扣减
     */
    @Override
    public Boolean operationStock(Integer id, Integer num, String type) {
        UpdateWrapper<StorePresale> updateWrapper = new UpdateWrapper<>();
        if (type.equals("add")) {
            updateWrapper.setSql(StrUtil.format("stock = stock + {}", num));
            updateWrapper.setSql(StrUtil.format("sales = sales - {}", num));
            updateWrapper.setSql(StrUtil.format("quota = quota + {}", num));
        }
        if (type.equals("sub")) {
            updateWrapper.setSql(StrUtil.format("stock = stock - {}", num));
            updateWrapper.setSql(StrUtil.format("sales = sales + {}", num));
            updateWrapper.setSql(StrUtil.format("quota = quota - {}", num));
            // 扣减时加乐观锁保证库存不为负
            updateWrapper.last(StrUtil.format(" and (quota - {} >= 0)", num));
        }
        updateWrapper.eq("id", id);
        boolean update = update(updateWrapper);
        if (!update) {
            throw new CrmebException("更新预售商品库存失败,商品id = " + id);
        }
        return update;
    }

    /**
     * 预售首页数据
     * 预售数据 + 预售商品6个
     * 3个用户头像（最多）
     * 预售参与总人数
     *
     * @return CombinationIndexResponse
     */
    @Override
    public PresaleIndexResponse getIndexInfo() {
        // 获取6个预售商品
        LambdaQueryWrapper<StorePresale> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresale::getIsDel, false);
        lqw.eq(StorePresale::getIsShow, true);
        long millis = System.currentTimeMillis();
        lqw.ge(StorePresale::getPayStopTime, millis);
        lqw.orderByDesc(StorePresale::getSort, StorePresale::getId);
        lqw.last(" limit 6");
        List<StorePresale> combinationList = dao.selectList(lqw);
        if (CollUtil.isEmpty(combinationList)) {
            return null;
        }
        combinationList.forEach(e -> {
            int percentIntVal = CrmebUtil.percentInstanceIntVal(e.getQuota(), e.getQuotaShow());
            e.setQuotaPercent(percentIntVal);
        });

        PresaleIndexResponse response = new PresaleIndexResponse();
        response.setProductList(combinationList);
        return response;
    }

    /**
     * 预售列表header
     *
     * @return CombinationHeaderResponse
     */
//    @Override
//    public CombinationHeaderResponse getHeader() {
//        // 获取最近的3单预售订单
//        List<StorePink> tempPinkList = storePinkService.findSizePink(7);
//        List<String> avatarList = CollUtil.newArrayList();
//        if (CollUtil.isNotEmpty(tempPinkList)) {
//            // 获取这三个用户头像
//            avatarList = tempPinkList.stream().map(StorePink::getAvatar).collect(Collectors.toList());
//        }
//        // 获取预售参与总人数
//        Integer totalPeople = storePinkService.getTotalPeople();
//
//        // 获取预售列表banner
//        List<HashMap<String, Object>> bannerList = systemGroupDataService.getListMapByGid(Constants.GROUP_DATA_ID_COMBINATION_LIST_BANNNER);
//
//        CombinationHeaderResponse response = new CombinationHeaderResponse();
//        response.setAvatarList(avatarList);
//        response.setTotalPeople(totalPeople);
//        response.setBannerList(bannerList);
//        return response;
//    }

    /**
     * 预售操作库存
     */
    private boolean doProductStock(StoreProductStockRequest storeProductStockRequest) {
        // 砍价商品信息回滚
        StorePresale existCombination = getById(storeProductStockRequest.getCombinationId());
        List<StoreProductAttrValue> existAttr =
                storeProductAttrValueService.getListByProductIdAndAttrId(
                        storeProductStockRequest.getCombinationId(),
                        storeProductStockRequest.getAttrId().toString(),
                        storeProductStockRequest.getType());
        if (ObjectUtil.isNull(existCombination) || ObjectUtil.isNull(existAttr)) { // 未找到商品
            logger.info("库存修改任务未获取到商品信息" + JSON.toJSONString(storeProductStockRequest));
            return true;
        }

        // 回滚商品库存/销量 并更新
        boolean isPlus = storeProductStockRequest.getOperationType().equals("add");
        int productStock = isPlus ? existCombination.getStock() + storeProductStockRequest.getNum() : existCombination.getStock() - storeProductStockRequest.getNum();
//        existCombination.setStock(productStock);
        existCombination.setSales(existCombination.getSales() - storeProductStockRequest.getNum());
        existCombination.setQuota(existCombination.getQuota() + storeProductStockRequest.getNum());
        updateById(existCombination);

        // 回滚sku库存
        for (StoreProductAttrValue attrValue : existAttr) {
            int productAttrStock = isPlus ? attrValue.getStock() + storeProductStockRequest.getNum() : attrValue.getStock() - storeProductStockRequest.getNum();
            attrValue.setStock(productAttrStock);
            attrValue.setSales(attrValue.getSales() - storeProductStockRequest.getNum());
            attrValue.setQuota(attrValue.getQuota() + storeProductStockRequest.getNum());
            storeProductAttrValueService.updateById(attrValue);
        }

        // 商品本身库存回滚
        // StoreProductStockRequest 创建次对象调用商品扣减库存实现扣减上本本身库存
        StoreProductResponse existProductLinkedSeckill = storeProductService.getByProductId(storeProductStockRequest.getProductId());
        for (StoreProductAttrValueResponse attrValueResponse : existProductLinkedSeckill.getAttrValue()) {
            if (attrValueResponse.getSuk().equals(storeProductStockRequest.getSuk())) {
                StoreProductStockRequest r = new StoreProductStockRequest()
                        .setAttrId(attrValueResponse.getId())
                        .setNum(storeProductStockRequest.getNum())
                        .setOperationType("add")
                        .setProductId(storeProductStockRequest.getProductId())
                        .setType(Constants.PRODUCT_TYPE_NORMAL)
                        .setSuk(storeProductStockRequest.getSuk());
                storeProductService.doProductStock(r);
            }
        }

        return true;
    }

    /**
     * 获取制式结构给attr属性
     */
    private List<HashMap<String, Object>> getSkuAttrList(List<StoreProductAttr> attrList) {
        List<HashMap<String, Object>> attrMapList = new ArrayList<>();
        if (CollUtil.isEmpty(attrList)) {
            return attrMapList;
        }
        for (StoreProductAttr attr : attrList) {
            HashMap<String, Object> attrMap = new HashMap<>();
            attrMap.put("productId", attr.getProductId());
            attrMap.put("attrName", attr.getAttrName());
            List<String> attrValues = new ArrayList<>();
            String trimAttr = attr.getAttrValues()
                    .replace("[", "")
                    .replace("]", "");
            if (attr.getAttrValues().contains(",")) {
                attrValues = Arrays.asList(trimAttr.split(","));
            } else {
                attrValues.add(trimAttr);
            }
            attrMap.put("attrValues", attrValues);

            List<HashMap<String, Object>> attrValueMapList = new ArrayList<>();
            for (String attrValue : attrValues) {
                HashMap<String, Object> attrValueMap = new HashMap<>();
                attrValueMap.put("attr", attrValue);
                attrValueMapList.add(attrValueMap);
            }
            attrMap.put("attrValue", attrValueMapList);
            attrMapList.add(attrMap);
        }
        return attrMapList;
    }
}

