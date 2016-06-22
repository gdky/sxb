package gov.hygs.htgl.entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Model class of 正式知识库.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Zszsk implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ID_. */
	private String id;

	/** 录入人ID. */
	private Integer userId;

	/** 创建日期. */
	private Date createDate;

	/** 审批日期. */
	private Date spDate;

	/** 审批人ID. */
	private Integer sprId;

	/** 部门ID. */
	private Integer deptid;

	/** 内容. */
	private String content;

	/** 来源ID. */
	private Integer zsklyId;

	/** 知识库标题. */
	private String title;

	/** 有效标志. */
	private String yxbz;
	
	public String getYxbz() {
		return yxbz;
	}

	public void setYxbz(String yxbz) {
		this.yxbz = yxbz;
	}

	/**
	 * Constructor.
	 */
	public Zszsk() {
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
	 * Set the 录入人ID.
	 * 
	 * @param userId
	 *            录入人ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Get the 录入人ID.
	 * 
	 * @return 录入人ID
	 */
	public Integer getUserId() {
		return this.userId;
	}

	/**
	 * Set the 创建日期.
	 * 
	 * @param createDate
	 *            创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Get the 创建日期.
	 * 
	 * @return 创建日期
	 */
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * Set the 审批日期.
	 * 
	 * @param spDate
	 *            审批日期
	 */
	public void setSpDate(Date spDate) {
		this.spDate = spDate;
	}

	/**
	 * Get the 审批日期.
	 * 
	 * @return 审批日期
	 */
	public Date getSpDate() {
		return this.spDate;
	}

	/**
	 * Set the 审批人ID.
	 * 
	 * @param sprId
	 *            审批人ID
	 */
	public void setSprId(Integer sprId) {
		this.sprId = sprId;
	}

	/**
	 * Get the 审批人ID.
	 * 
	 * @return 审批人ID
	 */
	public Integer getSprId() {
		return this.sprId;
	}

	/**
	 * Set the 部门ID.
	 * 
	 * @param deptid
	 *            部门ID
	 */
	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	/**
	 * Get the 部门ID.
	 * 
	 * @return 部门ID
	 */
	public Integer getDeptid() {
		return this.deptid;
	}

	/**
	 * Set the 内容.
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get the 内容.
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Set the 来源ID.
	 * 
	 * @param zsklyId
	 *            来源ID
	 */
	public void setZsklyId(Integer zsklyId) {
		this.zsklyId = zsklyId;
	}

	/**
	 * Get the 来源ID.
	 * 
	 * @return 来源ID
	 */
	public Integer getZsklyId() {
		return this.zsklyId;
	}

	/**
	 * Set the 知识库标题.
	 * 
	 * @param title
	 *            知识库标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the 知识库标题.
	 * 
	 * @return 知识库标题
	 */
	public String getTitle() {
		return this.title;
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
		Zszsk other = (Zszsk) obj;
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
