package com.jnu.dropshipplatform.controller;

import com.jnu.dropshipplatform.entity.BrandInfo;
import com.jnu.dropshipplatform.entity.CompanyInfo;
import com.jnu.dropshipplatform.service.BrandInfoService;
import com.jnu.dropshipplatform.service.CompanyInfoService;
import com.jnu.dropshipplatform.entity.*;
import com.jnu.dropshipplatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("jnu")
public class CompanyInfoController {

    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private BrandInfoService brandInfoService;

    @GetMapping("/company/{id}")
    public String addComInfo(@PathVariable("id") Integer comId,Model model,HttpSession session) {
        session.setAttribute("comId",comId);
        CompanyInfo com = companyInfoService.getCompanyById(comId);
        model.addAttribute("company",com);
        return "CompanyInfo";
    }

    @GetMapping("/providerInfo")
    public String getAllCom(Model model,HttpSession session) {
        String str = ""+session.getAttribute("comId");
        Integer comId = Integer.parseInt(str);
        CompanyInfo com = companyInfoService.getCompanyById(comId);
        model.addAttribute("company",com);
        Integer ownerId = comId;
        CompanyInfo companyInfo = companyInfoService.getCompanyInfoById(ownerId);
        session.setAttribute("owner",companyInfo);
        List<BrandInfo> brandInfos = brandInfoService.getAllBrand(ownerId);
        model.addAttribute("allBrand",brandInfos);
        return "ProviderInfo";
    }

    @GetMapping("/company")
    public String updatePage(Model model, HttpSession session) {
        String str = ""+session.getAttribute("comId");
        Integer comId = Integer.parseInt(str);
        CompanyInfo com = companyInfoService.getCompanyById(comId);
        model.addAttribute("company",com);
        return "CompanyInfo";
    }

    @PostMapping("/company")
    public String updateCompany(CompanyInfo companyInfo,HttpSession session) {
        String str = ""+session.getAttribute("comId");
        Integer comId = Integer.parseInt(str);
        companyInfo.setUserComId(comId);
        companyInfo.setComBalance(0.0);
        companyInfoService.updateCompanyInfo(companyInfo);
        return "redirect:/jnu/providerInfo";
    }

    //BrandInfo begin here....
    @GetMapping("providerInfo/add")
    public String addBrandInfoPage(){
        return "addBrandInfo";
    }

    @PostMapping("providerInfo/add")
    public String addBrandInfo(BrandInfo brandInfo,HttpSession session){
        CompanyInfo owner = (CompanyInfo) session.getAttribute("owner");
        brandInfo.setBrandOwner(owner);
        brandInfoService.addBrandInfo(brandInfo);
        return "redirect:/jnu/providerInfo";
    }

    @GetMapping("providerInfo/{id}/delete")
    public String deleteBrandInfo(@PathVariable("id") Integer brandId,HttpSession session){
        brandInfoService.deleteBrandInfo(brandId);
        CompanyInfo owner = (CompanyInfo) session.getAttribute("owner");
        return "redirect:/jnu/providerInfo";
    }

    @GetMapping("providerInfo/{id}/update")
    public String updateBrandInfoPage(@PathVariable("id") Integer brandId,Model model){
        BrandInfo brandInfo = brandInfoService.getBrandInfoByBrandId(brandId);
        model.addAttribute("brandInfo",brandInfo);
        return "updateBrandInfo";
    }

    @PostMapping("providerInfo/update")
    public String updateBrandInfo(BrandInfo brandInfo,HttpSession session){
        CompanyInfo owner = (CompanyInfo)session.getAttribute("owner");
        brandInfo.setBrandOwner(owner);
        brandInfoService.updateBrandInfo(brandInfo);
        return "redirect:/jnu/providerInfo";
    }

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("jnu/company")
public class CompanyInfoController {
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private BrandInfoService brandInfoService;
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private BrandProductService brandProductService;

    @GetMapping("/ProductShow/{id}")
    public String getProduct(/*HttpSession session,*/@PathVariable("id") Integer id, Model model){
        //CompanyInfo companyInfo=session.getAttribute("CompanyInfo");
        CompanyInfo companyInfo=companyInfoService.findCompanyInfoByUserComId(id);
        List<BrandInfo> brandInfos=brandInfoService.findBrandInfoByBrandOwner(companyInfo);
        List<BrandProduct> brandProducts=new ArrayList<BrandProduct>();
        List<ProductAndCategory> product=new ArrayList<ProductAndCategory>();
        for(int i=0;i<brandInfos.size();i++){
            brandProducts.addAll(brandProductService.findBrandProductByBrandId(brandInfos.get(i).getBrandId()));
        }
        for(int i=0;i<brandProducts.size();i++){
            product.addAll(productInfoService.getProductAndCategory(brandProducts.get(i).getProductInfo()));
        }
        model.addAttribute("product",product);
        return "CompanyProductShow";
    }

    @GetMapping("insert")
    public String jumpToInsert(/*HttpSession session,Model model*/){

    return "CompanyProductInsert";
    }
    @PostMapping("insert")
    public  String insert(ProductInfo productInfo){
        productInfo.setProStatus(0);
        productInfoService.save(productInfo);
        BrandProduct brandProduct=new BrandProduct();
        brandProduct.setBrandId(1);
        brandProduct.setProductInfo(productInfo.getProId());
        brandProductService.save(brandProduct);
        return "redirect/jnu/company/CompanyProductShow";
    }

    @GetMapping("update/{id}")
    public String jumpToUpdate(@PathVariable ("id") Integer id,Model model){
        ProductInfo productInfo=productInfoService.findProductInfoByProId(id);
        model.addAttribute("product",productInfo);
        return "CompanyProductUpdate";
    }
    @PostMapping("update")
    public  String update(ProductInfo productInfo){
        productInfoService.save(productInfo);
        return "redirect/jnu/company/CompanyProductShow";
    }
}
