package gov.hygs.htgl.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Model class of 个人题库贡献记录.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class GrTkGxjl implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ID_. */
	private String id;

	/** 部门ID. */
	private Integer deptId;

	/** 题目ID. */
	private String tkId;

	/** 贡献值. */
	private Double gxz;

	/** 贡献方式 1预选库出题，2被选入正式库. */
	private Integer gxly;

	/** 获取时间. */
	private Date gxDate;

	/**
	 * Constructor.
	 */
	public GrTkGxjl() {
	}

	/**
	 * Set the ID_.
	 * 
	 * @param id
	 *            ID_
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the ID_.
	 * 
	 * @return ID_
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set the 部门ID.
	 * 
	 * @param deptId
	 *            部门ID
	 */
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	/**
	 * Get the 部门ID.
	 * 
	 * @return 部门ID
	 */
	public Integer getDeptId() {
		return this.deptId;
	}

	/**
	 * Set the 题目ID.
	 * 
	 * @param tkId
	 *            题目ID
	 */
	public void setTkId(String tkId) {
		this.tkId = tkId;
	}

	/**
	 * Get the 题目ID.
	 * 
	 * @return 题目ID
	 */
	public String getTkId() {
		return this.tkId;
	}

	/**
	 * Set the 贡献值.
	 * 
	 * @param gxz
	 *            贡献值
	 */
	public void setGxz(Double gxz) {
		this.gxz = gxz;
	}

	/**
	 * Get the 贡献值.
	 * 
	 * @return 贡献值
	 */
	public Double getGxz() {
		return this.gxz;
	}

	/**
	 * Set the 贡献方式 1预选库出题，2被选入正式库.
	 * 
	 * @param gxly
	 *            贡献方式 1预选库出题，2被选入正式库
	 */
	public void setGxly(Integer gxly) {
		this.gxly = gxly;
	}

	/**
	 * Get the 贡献方式 1预选库出题，2被选入正式库.
	 * 
	 * @return 贡献方式 1预选库出题，2被选入正式库
	 */
	public Integer getGxly() {
		return this.gxly;
	}

	/**
	 * Set the 获取时间.
	 * 
	 * @param gxDate
	 *            获取时间
	 */
	public void setGxDate(Date gxDate) {
		this.gxDate = gxDate;
	}

	/**
	 * Get the 获取时间.
	 * 
	 * @return 获取时间
	 */
	public Date getGxDate() {
		return this.gxDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GrTkGxjl other = (GrTkGxjl) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
