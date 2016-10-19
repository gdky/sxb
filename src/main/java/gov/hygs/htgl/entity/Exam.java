package gov.hygs.htgl.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Model class of 考试.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Exam implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ID_. */
	private Integer id;

	/** 开始时间. */
	private Date startTime;

	/** 结束时间. */
	private Date endTime;

	/** 考试主题. */
	private String title;

	/** 考试类型 1正式考试2抢答. */
	private String examType;

	/** 发起人ID. */
	private Integer fqrId;

	private String remark;
	
	private Integer examTime;
	
	/**
	 * Constructor.
	 */
	public Exam() {
	}

	/**
	 * Set the ID_.
	 * 
	 * @param id
	 *            ID_
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the ID_.
	 * 
	 * @return ID_
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Set the 开始时间.
	 * 
	 * @param startTime
	 *            开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get the 开始时间.
	 * 
	 * @return 开始时间
	 */
	public Date getStartTime() {
		return this.startTime;
	}

	/**
	 * Set the 结束时间.
	 * 
	 * @param endTime
	 *            结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Get the 结束时间.
	 * 
	 * @return 结束时间
	 */
	public Date getEndTime() {
		return this.endTime;
	}

	/**
	 * Set the 考试主题.
	 * 
	 * @param title
	 *            考试主题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the 考试主题.
	 * 
	 * @return 考试主题
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set the 考试类型 1正式考试2抢答.
	 * 
	 * @param examType
	 *            考试类型 1正式考试2抢答
	 */
	public void setExamType(String examType) {
		this.examType = examType;
	}

	/**
	 * Get the 考试类型 1正式考试2抢答.
	 * 
	 * @return 考试类型 1正式考试2抢答
	 */
	public String getExamType() {
		return this.examType;
	}

	/**
	 * Set the 发起人ID.
	 * 
	 * @param fqrId
	 *            发起人ID
	 */
	public void setFqrId(Integer fqrId) {
		this.fqrId = fqrId;
	}

	/**
	 * Get the 发起人ID.
	 * 
	 * @return 发起人ID
	 */
	public Integer getFqrId() {
		return this.fqrId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getExamTime() {
		return examTime;
	}

	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
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
		Exam other = (Exam) obj;
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
