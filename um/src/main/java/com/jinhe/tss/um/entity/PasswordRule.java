package com.jinhe.tss.um.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.util.BeanUtil;

/**
 * 定义密码规则。
 */
@Entity
@Table(name = "um_passwordrule")
@SequenceGenerator(name = "passwordrule_sequence", sequenceName = "passwordrule_sequence", initialValue = 100, allocationSize = 3)
public class PasswordRule implements IEntity, ITreeNode, IXForm{
	
	@Transient public static final String UNQUALIFIED_LEVEL = "0";	//不可用
	@Transient public static final String LOW_LEVEL         = "1";	//低
	@Transient public static final String MEDIUM_LEVEL      = "2";	//中
	@Transient public static final String HIGHER_LEVEL      = "3";	//高
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "passwordrule_sequence")
	private Long id;
	private String name;
	
	private Integer isDefault = UMConstants.FALSE;
	
	private Long leastLength;		 // 最短长度
	private Integer canEq2LoginName; // 是否可以和用户名相同
	
	private Long leastStrength;	     // 最低强度
	private Long lowStrength;	     // 低强度临界值
	private Long higherStrength;     // 高强度
	private String impermissible;    // 不允许的密码，用","隔开

	public void copyAttribute(PasswordRule rule){
		name = rule.getName();
		leastLength = rule.getLeastLength();
		canEq2LoginName = rule.getCanEq2LoginName();
		leastStrength = rule.getLeastStrength();
		lowStrength = rule.getLowStrength();
		higherStrength = rule.getHigherStrength();
		impermissible = rule.getImpermissible();
	}
	
	public static PasswordRule getDefaultPasswordRule(){
		PasswordRule rule = new PasswordRule();
		rule.canEq2LoginName = UMConstants.FALSE;
		rule.leastLength    = new Long(6);
		rule.leastStrength  = new Long(8);
		rule.lowStrength    = new Long(16);
		rule.higherStrength = new Long(60);
		return rule;
	}
 
	public Integer getCanEq2LoginName() {
		return canEq2LoginName;
	}
 
	public Long getHigherStrength() {
		return higherStrength;
	}
 
	public Long getId() {
		return id;
	}
 
	public String getImpermissible() {
		return impermissible;
	}
 
	public Long getLeastLength() {
		return leastLength;
	}
 
	public Long getLeastStrength() {
		return leastStrength;
	}
 
	public Long getLowStrength() {
		return lowStrength;
	}
 
	public String getName() {
		return name;
	}
 
	public Integer getIsDefault() {
		return isDefault;
	}
 
	public void setCanEq2LoginName(Integer canEq2LoginName) {
		this.canEq2LoginName = canEq2LoginName;
	}
 
	public void setHigherStrength(Long higherStrength) {
		this.higherStrength = higherStrength;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public void setImpermissible(String impermissible) {
		this.impermissible = impermissible;
	}
 
	public void setLeastLength(Long leastLength) {
		this.leastLength = leastLength;
	}
 
	public void setLeastStrength(Long leastStrength) {
		this.leastStrength = leastStrength;
	}
 
	public void setLowStrength(Long lowStrength) {
		this.lowStrength = lowStrength;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public TreeAttributesMap getAttributes() {
		TreeAttributesMap map = new TreeAttributesMap(id, name);
		map.put("leastLength", leastLength);
		map.put("leastStrength", leastStrength);
		map.put("lowStrength", lowStrength);
		map.put("higherStrength", higherStrength);
		map.put("impermissible", impermissible);
		return map;
	}

	public Map<String, Object> getAttributesForXForm() {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanUtil.addBeanProperties2Map(this, map);
		return map;
	}
	
	
	// 密码强度定义.
    static int factor[] = {1, 2, 3, 4};
    static int kindFactor[] = {0, 0, 30, 50, 70};
    static String[] regex = {"0123456789", 
                             "abcdefghijklmnopqrstuvwxyz", 
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 
                             "~`!@#$%^&*()-=_+,./<>?;:|"};

    public static int getStrengthValue(String pwd) {
        int strengthValue = 0;
        int composedKind = 0;
        for (int i = 0; i < regex.length; i++) {
            int matched = 0;
            for (int j = 0; j < pwd.length(); j++) {
                if (regex[i].indexOf(pwd.charAt(j)) >= 0)
                    matched++;
            }
 
            if (matched != 0) {
                strengthValue += matched * factor[i];
                composedKind++;
            }
        }
        strengthValue += kindFactor[composedKind];
        return strengthValue;
    }
}
