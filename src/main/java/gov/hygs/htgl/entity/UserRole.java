package gov.hygs.htgl.entity;

import java.io.Serializable;

/**
 * Model class of 用户角色表.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class UserRole implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ID_. */
	private Integer id_;

	/** user_id. */
	private Integer user_Id;

	/** role_id. */
	private Integer role_Id;

	/**
	 * Constructor.
	 */
	public UserRole() {
	}

	/**
	 * Set the ID_.
	 * 
	 * @param id
	 *            ID_
	 */
	public void setId_(Integer id_) {
		this.id_ = id_;
	}

	/**
	 * Get the ID_.
	 * 
	 * @return ID_
	 */
	public Integer getId_() {
		return this.id_;
	}

	/**
	 * Set the user_id.
	 * 
	 * @param user_Id
	 *            user_id
	 */
	public void setUser_Id(Integer user_Id) {
		this.user_Id = user_Id;
	}

	/**
	 * Get the user_id.
	 * 
	 * @return user_id
	 */
	public Integer getUser_Id() {
		return this.user_Id;
	}

	/**
	 * Set the role_id.
	 * 
	 * @param role_Id
	 *            role_id
	 */
	public void setRole_Id(Integer role_Id) {
		this.role_Id = role_Id;
	}

	/**
	 * Get the role_id.
	 * 
	 * @return role_id
	 */
	public Integer getRole_Id() {
		return this.role_Id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_ == null) ? 0 : id_.hashCode());
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
		UserRole other = (UserRole) obj;
		if (id_ == null) {
			if (other.id_ != null) {
				return false;
			}
		} else if (!id_.equals(other.id_)) {
			return false;
		}
		return true;
	}

}
