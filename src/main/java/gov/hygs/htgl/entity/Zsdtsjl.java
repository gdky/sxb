package gov.hygs.htgl.entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Model class of 知识点推送记录.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Zsdtsjl implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ID_. */
	private Integer id;

	/** 推送人. */
	private Integer tsrid;

	/** 推送日期. */
	private Date tsrq;

	/** 描述. */
	private String ms;

	/**
	 * Constructor.
	 */
	public Zsdtsjl() {
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
	 * Set the 推送人.
	 * 
	 * @param tsrid
	 *            推送人
	 */
	public void setTsrid(Integer tsrid) {
		this.tsrid = tsrid;
	}

	/**
	 * Get the 推送人.
	 * 
	 * @return 推送人
	 */
	public Integer getTsrid() {
		return this.tsrid;
	}

	/**
	 * Set the 推送日期.
	 * 
	 * @param tsrq
	 *            推送日期
	 */
	public void setTsrq(Date tsrq) {
		this.tsrq = tsrq;
	}

	/**
	 * Get the 推送日期.
	 * 
	 * @return 推送日期
	 */
	public Date getTsrq() {
		return this.tsrq;
	}

	/**
	 * Set the 描述.
	 * 
	 * @param ms
	 *            描述
	 */
	public void setMs(String ms) {
		this.ms = ms;
	}

	/**
	 * Get the 描述.
	 * 
	 * @return 描述
	 */
	public String getMs() {
		return this.ms;
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
		Zsdtsjl other = (Zsdtsjl) obj;
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