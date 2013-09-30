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
public class MpcaProductWebPagePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MPCA_PRODUCT_PRODUCT_ID")
    private long mpcaProductProductId;
    @Basic(optional = false)
    @Column(name = "MPCA_WEB_PAGE_PAGE_ID")
    private long mpcaWebPagePageId;

    public MpcaProductWebPagePK() {
    }

    public MpcaProductWebPagePK(long mpcaProductProductId, long mpcaWebPagePageId) {
        this.mpcaProductProductId = mpcaProductProductId;
        this.mpcaWebPagePageId = mpcaWebPagePageId;
    }

    public long getMpcaProductProductId() {
        return mpcaProductProductId;
    }

    public void setMpcaProductProductId(long mpcaProductProductId) {
        this.mpcaProductProductId = mpcaProductProductId;
    }

    public long getMpcaWebPagePageId() {
        return mpcaWebPagePageId;
    }

    public void setMpcaWebPagePageId(long mpcaWebPagePageId) {
        this.mpcaWebPagePageId = mpcaWebPagePageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mpcaProductProductId;
        hash += (int) mpcaWebPagePageId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductWebPagePK)) {
            return false;
        }
        MpcaProductWebPagePK other = (MpcaProductWebPagePK) object;
        if (this.mpcaProductProductId != other.mpcaProductProductId) {
            return false;
        }
        if (this.mpcaWebPagePageId != other.mpcaWebPagePageId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductWebPagePK[ mpcaProductProductId=" + mpcaProductProductId + ", mpcaWebPagePageId=" + mpcaWebPagePageId + " ]";
    }
    
}
