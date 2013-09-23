/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Antonio
 */
@Embeddable
public class MpcaProductIndexPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MPCA_PRODUCT_PRODUCT_ID")
    private long mpcaProductProductId;
    @Basic(optional = false)
    @Column(name = "MPCA_INDEX_TYPE_INDEX_ID")
    private long mpcaIndexTypeIndexId;

    public MpcaProductIndexPK() {
    }

    public MpcaProductIndexPK(long mpcaProductProductId, long mpcaIndexTypeIndexId) {
        this.mpcaProductProductId = mpcaProductProductId;
        this.mpcaIndexTypeIndexId = mpcaIndexTypeIndexId;
    }

    public long getMpcaProductProductId() {
        return mpcaProductProductId;
    }

    public void setMpcaProductProductId(long mpcaProductProductId) {
        this.mpcaProductProductId = mpcaProductProductId;
    }

    public long getMpcaIndexTypeIndexId() {
        return mpcaIndexTypeIndexId;
    }

    public void setMpcaIndexTypeIndexId(long mpcaIndexTypeIndexId) {
        this.mpcaIndexTypeIndexId = mpcaIndexTypeIndexId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mpcaProductProductId;
        hash += (int) mpcaIndexTypeIndexId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductIndexPK)) {
            return false;
        }
        MpcaProductIndexPK other = (MpcaProductIndexPK) object;
        if (this.mpcaProductProductId != other.mpcaProductProductId) {
            return false;
        }
        if (this.mpcaIndexTypeIndexId != other.mpcaIndexTypeIndexId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductIndexPK[ mpcaProductProductId=" + mpcaProductProductId + ", mpcaIndexTypeIndexId=" + mpcaIndexTypeIndexId + " ]";
    }
    
}
