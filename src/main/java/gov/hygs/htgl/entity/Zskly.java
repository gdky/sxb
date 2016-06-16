package gov.hygs.htgl.entity;
import java.io.Serializable;

/**
 * Model class of 知识库来源表.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Zskly implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ID_. */
	private Integer id;

	/** 来源标题. */
	private String title;

	/** 内容. */
	private String content;

	/** 附件. */
	private String attachment;

	/**
	 * Constructor.
	 */
	public Zskly() {
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
	 * Set the 来源标题.
	 * 
	 * @param title
	 *            来源标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the 来源标题.
	 * 
	 * @return 来源标题
	 */
	public String getTitle() {
		return this.title;
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
	 * Set the 附件.
	 * 
	 * @param attachment
	 *            附件
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	/**
	 * Get the 附件.
	 * 
	 * @return 附件
	 */
	public String getAttachment() {
		return this.attachment;
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
		Zskly other = (Zskly) obj;
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
