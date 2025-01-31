package com.marcosimon.autosurvey.msforginfo;

import com.marcosimon.autosurvey.allowanceinfo.AllowanceInfo;
import com.marcosimon.autosurvey.allowanceinfo.IAllowanceInfoDbRepository;
import com.marcosimon.autosurvey.allowancepercentinfo.AllowancePercentInfo;
import com.marcosimon.autosurvey.allowancepercentinfo.IAllowancePercentInfoDbRepository;
import com.marcosimon.autosurvey.contactinfo.ContactInfo;
import com.marcosimon.autosurvey.contactinfo.IContactInfoDbRepository;
import com.marcosimon.autosurvey.countryinfo.CountryInfo;
import com.marcosimon.autosurvey.countryinfo.ICountryInfoDbRepository;
import com.marcosimon.autosurvey.currencyinfo.CurrencyInfo;
import com.marcosimon.autosurvey.currencyinfo.ICurrencyInfoDbRepository;
import com.marcosimon.autosurvey.exception.CustomException;
import com.marcosimon.autosurvey.functioninfo.FunctionInfo;
import com.marcosimon.autosurvey.functioninfo.IFunctionInfoRepository;
import com.marcosimon.autosurvey.functionsalaryinfo.FunctionSalaryInfo;
import com.marcosimon.autosurvey.functionsalaryinfo.IFunctionSalaryInfoDbRepository;
import com.marcosimon.autosurvey.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.marcosimon.autosurvey.constants.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MsfOrgInfoService {

  private final IMsfOrgInfoDbRepository msfOrgInfoDbRepository;
  private final ICountryInfoDbRepository countryInfoDbRepository;
  private final ICurrencyInfoDbRepository currencyInfoDbRepository;
  private final IContactInfoDbRepository contactInfoDbRepository;
  private final IFunctionInfoRepository functionInfoDbRepository;
  private final IFunctionSalaryInfoDbRepository functionSalaryInfoDbRepository;
  private final IAllowanceInfoDbRepository allowanceInfoDbRepository;
  private final IAllowancePercentInfoDbRepository allowancePercentInfoDbRepository;

  public FinalOrgInfoDTO getFinalOrgInfo(NewFinalOrgInfoDTO newFinalOrgInfoDTO) {
    CountryInfo countryInfo = countryInfoDbRepository
            .findByNameAndYear(newFinalOrgInfoDTO.countryName(), newFinalOrgInfoDTO.year())
            .orElseThrow(() -> new CustomException(COUNTRY_INFO_NOT_FOUND));

    CurrencyInfo currencyInfo = currencyInfoDbRepository
            .findById(countryInfo.getCountryInfoId()).orElseThrow(() -> new CustomException(CURRENCY_INFO_NOT_FOUND));

    MsfOrgInfo msfOrgInfo = msfOrgInfoDbRepository
            .findByOrgNameAndCountryInfo(newFinalOrgInfoDTO.orgName(), countryInfo).orElseThrow(() -> new CustomException(ORG_INFO_NOT_FOUND));

    ContactInfo contactInfo = contactInfoDbRepository
            .findById(msfOrgInfo.getOrgId()).orElseThrow(() -> new CustomException(CONTACT_INFO_NOT_FOUND));

    FunctionInfo functionInfo = functionInfoDbRepository
            .findById(newFinalOrgInfoDTO.functionId()).orElseThrow(() -> new CustomException(FUNCTION_INFO_NOT_FOUND));

    FunctionSalaryInfo functionSalaryInfo = functionSalaryInfoDbRepository
            .findByOrgFunctionIdAndFunctionAndOrg(newFinalOrgInfoDTO.orgFunctionId(), functionInfo, msfOrgInfo)
            .orElseThrow(() -> new CustomException(FUNCTION_SALARY_INFO_NOT_FOUND));

    AllowanceInfo allowanceInfo = allowanceInfoDbRepository
            .findById(msfOrgInfo.getOrgId()).orElseThrow(() -> new CustomException(ALLOWANCE_INFO_NOT_FOUND));

    AllowancePercentInfo allowancePercentInfo = allowancePercentInfoDbRepository
            .findById(msfOrgInfo.getOrgId()).orElseThrow(() -> new CustomException(ALLOWANCE_PERCENT_INFO_NOT_FOUND));

    return new FinalOrgInfoDTO(countryInfo.getCountryName(), countryInfo.getYear(), msfOrgInfo.getOrgName(), msfOrgInfo.getOrgFullName(), functionInfo.getMsfLevel(), functionInfo.getIrffgLevel(),
            functionInfo.getFunctionInfoId(), functionInfo.getMsfFunction(),functionSalaryInfo.getOrgFunctionId(), functionSalaryInfo.getOrgFunction(), countryInfo.getCurrencyRef(), msfOrgInfo.getCurrencyInUse(), currencyInfo.getCurrency(), currencyInfo.getExchangeRate(),
            msfOrgInfo.getWorkingHours(), msfOrgInfo.getThirteenthSalary(), functionSalaryInfo.getBasicSalary(), functionSalaryInfo.getAllowancePerFunction(),
            allowanceInfo.getColAllowance(), allowanceInfo.getTransportationAllowance(), allowanceInfo.getHousingAllowance(), allowanceInfo.getOtherAllowance(),
            allowanceInfo.getTotalAllowance(), allowancePercentInfo.getColAllowancePercent(), allowancePercentInfo.getTransportationAllowancePercent(),
            allowancePercentInfo.getHousingAllowancePercent(), allowancePercentInfo.getOtherAllowancePercent(), allowancePercentInfo.getTotalAllowancePercent(),
            functionSalaryInfo.getTgc());

  }

  public List<FinalOrgInfoDTO> getAllFinalOrgInfo() { return msfOrgInfoDbRepository.findAllFinalOrgInfo();}

  public List<MsfOrgInfo> getAllMsfOrgInfo() {
    return Streamable.of(msfOrgInfoDbRepository.findAll()).toList();
  }

  public MsfOrgInfo getMsfOrgInfoById(Long id) {
    return msfOrgInfoDbRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ORG_INFO_NOT_FOUND));
  }

  @Transactional
  public synchronized MsfOrgInfo addMsfOrgInfo(NewMsfOrgInfoDTO newMsfOrgInfoDTO) {
    CountryInfo countryInfo = countryInfoDbRepository
                    .findByNameAndYear(newMsfOrgInfoDTO.countryName(), newMsfOrgInfoDTO.year())
            .orElseThrow(() -> new CustomException(COUNTRY_INFO_NOT_FOUND));

    msfOrgInfoDbRepository
            .findByOrgNameAndCountryInfo(newMsfOrgInfoDTO.orgName(), countryInfo)
            .ifPresent( info -> {
              throw new CustomException(ALREADY_SAVED_ORGANIZATION);
            });

    return msfOrgInfoDbRepository.save(new MsfOrgInfo(newMsfOrgInfoDTO.orgFullName(),
            newMsfOrgInfoDTO.orgName(),
            newMsfOrgInfoDTO.orgType(),
            newMsfOrgInfoDTO.dataCollectionDate(),
            newMsfOrgInfoDTO.workingHours(),
            newMsfOrgInfoDTO.thirteenthSalary(),
            newMsfOrgInfoDTO.currencyInUse(),
            countryInfo));
  }

  @Transactional
  public synchronized MsfOrgInfo updateMsfOrgInfo(Long id, NewMsfOrgInfoDTO updateMsfOrgInfoDTO) {
    MsfOrgInfo storedOrgInfo = msfOrgInfoDbRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ORG_INFO_NOT_FOUND));

    if (updateMsfOrgInfoDTO.orgFullName() != null && !updateMsfOrgInfoDTO.orgFullName().isEmpty()) {
      storedOrgInfo.setOrgFullName(updateMsfOrgInfoDTO.orgFullName());
    }
    if (updateMsfOrgInfoDTO.orgName() != null && !updateMsfOrgInfoDTO.orgName().isEmpty()) {
      storedOrgInfo.setOrgName(updateMsfOrgInfoDTO.orgName());
    }
    if (updateMsfOrgInfoDTO.orgType() != null && !updateMsfOrgInfoDTO.orgType().isEmpty()) {
      storedOrgInfo.setOrgType(updateMsfOrgInfoDTO.orgType());
    }
    if (updateMsfOrgInfoDTO.dataCollectionDate() != null && !updateMsfOrgInfoDTO.dataCollectionDate().isEmpty()) {
      storedOrgInfo.setDataCollectionDate(updateMsfOrgInfoDTO.dataCollectionDate());
    }
    if (updateMsfOrgInfoDTO.workingHours() != null && updateMsfOrgInfoDTO.workingHours() >= 0) {
      storedOrgInfo.setWorkingHours(updateMsfOrgInfoDTO.workingHours());
    }
    if (updateMsfOrgInfoDTO.thirteenthSalary() != null && updateMsfOrgInfoDTO.thirteenthSalary() >= 0) {
      storedOrgInfo.setThirteenthSalary(updateMsfOrgInfoDTO.thirteenthSalary());
    }

    return msfOrgInfoDbRepository.save(storedOrgInfo);
  }

  public void deleteMsfOrgInfo(Long id) {
    msfOrgInfoDbRepository.findById(id).orElseThrow(() -> new CustomException(ORG_INFO_NOT_FOUND));
    msfOrgInfoDbRepository.deleteById(id);
  }

}
