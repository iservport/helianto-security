package org.helianto.security.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.helianto.core.domain.Identity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@javax.persistence.Entity
@Table(name="core_providerAuth",
uniqueConstraints = {
	 @UniqueConstraint(columnNames={"identityId", "providerType"})
}
)
public class ProviderAuthority 
	implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Version
    private int version;
    
    @JsonIgnore 
    @ManyToOne
    @JoinColumn(name="identityId", nullable=true)
    private Identity identity;
    
    @Column(length=20)
    private String providerType = "";
    
    @Column(length=40)
    private String providerUserId = "";
    
    /**
     * Constructor.
     */
    public ProviderAuthority() {
		super();
	}
    
    /**
     * Constructor.
     *
     * @param identity
     * @param providerType
     */
    public ProviderAuthority(Identity identity, String providerType) {
		this();
		setIdentity(identity);
		setProviderType(providerType);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

	public Identity getIdentity() {
		return identity;
	}
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public String getProviderType() {
		return providerType;
	}
	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public String getProviderUserId() {
		return providerUserId;
	}
	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identity == null) ? 0 : identity.hashCode());
		result = prime * result
				+ ((providerType == null) ? 0 : providerType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProviderAuthority other = (ProviderAuthority) obj;
		if (identity == null) {
			if (other.identity != null)
				return false;
		} else if (!identity.equals(other.identity))
			return false;
		if (providerType == null) {
			if (other.providerType != null)
				return false;
		} else if (!providerType.equals(other.providerType))
			return false;
		return true;
	}

}
