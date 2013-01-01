package com.jinhe.tss.um.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.persistence.entityaop.IDecodable;

/**
 * 用户与用户组关联对象
 */
@Entity
@Table(name = "um_groupuser", uniqueConstraints = { 
        @UniqueConstraint(columnNames = { "groupId", "userId" })
})
@SequenceGenerator(name = "groupuser_sequence", sequenceName = "groupuser_sequence", initialValue = 10000, allocationSize = 10)
public class GroupUser implements IDecodable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "groupuser_sequence")
	private Long id;  
	
	@Column(nullable = false)
	private Long groupId; // 用户组ID
	
	@Column(nullable = false)
	private Long userId;  // 用户ID
    
	@Column(nullable = false)
	private Integer seqNo;   // 用户排序号
	private String  decode;  // 层码
	private Integer levelNo; // 层次值
    
    public GroupUser() { }
    
    public GroupUser(Long userId, Long groupId, Integer seqNo){
        this.userId = userId;
        this.groupId = groupId;
        this.seqNo = seqNo;
    }
 
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public Long getGroupId() {
		return groupId;
	}
 
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
 
	public Long getUserId() {
		return userId;
	}
 
	public void setUserId(Long userId) {
		this.userId = userId;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer userOrder) {
		this.seqNo = userOrder;
	}
 
	public String getDecode() {
		return decode;
	}
 
	public void setDecode(String decode) {
		this.decode = decode;
	}
 
	public Integer getLevelNo() {
		return levelNo;
	}
 
	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}

	public Long getParentId() {
		return groupId;
	}

	public Class<?> getParentClass() {
		return Group.class;
	}
}
