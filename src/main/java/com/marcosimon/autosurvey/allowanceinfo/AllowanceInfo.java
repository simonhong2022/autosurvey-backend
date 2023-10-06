package com.marcosimon.autosurvey.allowanceinfo;

import com.marcosimon.autosurvey.msforginfo.MsfOrgInfo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "allowance_info")
public class AllowanceInfo {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "allowance_info_id")
    private String allowanceInfoId;

    @MapsId
    @OneToOne(mappedBy = "allowance")
    @JoinColumn(name = "allowance_info_id")
    private MsfOrgInfo msfOrgInfo;

    @Column(name ="cost_of_living_allowance")
    private Integer colAllowance;

    @Column(name ="transportation_allowance")
    private Integer transportationAllowance;

    @Column(name ="housing_allowance")
    private Integer housingAllowance;

    @Column(name ="other_allowance")
    private Integer otherAllowance;

    @Column(name ="total_allowance")
    private Integer totalAllowance;

    public AllowanceInfo(Integer colAllowance, Integer transportationAllowance, Integer housingAllowance, Integer otherAllowance, Integer totalAllowance) {
        this.colAllowance = colAllowance;
        this.transportationAllowance = transportationAllowance;
        this.housingAllowance = housingAllowance;
        this.otherAllowance = otherAllowance;
        this.totalAllowance = totalAllowance;
    }

    public AllowanceInfo(String allowanceInfoId, Integer colAllowance, Integer transportationAllowance, Integer housingAllowance, Integer otherAllowance, Integer totalAllowance) {
        this.allowanceInfoId = allowanceInfoId;
        this.colAllowance = colAllowance;
        this.transportationAllowance = transportationAllowance;
        this.housingAllowance = housingAllowance;
        this.otherAllowance = otherAllowance;
        this.totalAllowance = totalAllowance;
    }
}
