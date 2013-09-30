/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Antonio
 */
@Embeddable
public class MpcaProductAdditionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MPCA_PRODUCT_PRODUCT_ID")
    private long mpcaProductProductId;
    @Basic(optional = false)
    @Column(name = "MPCA_ADDITION_TYPE_ADD_ID")
    private long mpcaAdditionTypeAddId;

    public MpcaProductAdditionPK() {
    }

    public MpcaProductAdditionPK(long mpcaProductProductId, long mpcaAdditionTypeAddId) {
        this.mpcaProductProductId = mpcaProductProductId;
        this.mpcaAdditionTypeAddId = mpcaAdditionTypeAddId;
    }

    public long getMpcaProductProductId() {
        return mpcaProductProductId;
    }

    public void setMpcaProductProductId(long mpcaProductProductId) {
        this.mpcaProductProductId = mpcaProductProductId;
    }

    public long getMpcaAdditionTypeAddId() {
        return mpcaAdditionTypeAddId;
    }

    public void setMpcaAdditionTypeAddId(long mpcaAdditionTypeAddId) {
        this.mpcaAdditionTypeAddId = mpcaAdditionTypeAddId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mpcaProductProductId;
        hash += (int) mpcaAdditionTypeAddId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductAdditionPK)) {
            return false;
        }
        MpcaProductAdditionPK other = (MpcaProductAdditionPK) object;
        if (this.mpcaProductProductId != other.mpcaProductProductId) {
            return false;
        }
        if (this.mpcaAdditionTypeAddId != other.mpcaAdditionTypeAddId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductAdditionPK[ mpcaProductProductId=" + mpcaProductProductId + ", mpcaAdditionTypeAddId=" + mpcaAdditionTypeAddId + " ]";
    }
    
}
