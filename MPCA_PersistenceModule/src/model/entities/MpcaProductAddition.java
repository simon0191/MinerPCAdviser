/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_PRODUCT_ADDITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaProductAddition.findAll", query = "SELECT m FROM MpcaProductAddition m"),
    @NamedQuery(name = "MpcaProductAddition.findByMpcaProductProductId", query = "SELECT m FROM MpcaProductAddition m WHERE m.mpcaProductAdditionPK.mpcaProductProductId = :mpcaProductProductId"),
    @NamedQuery(name = "MpcaProductAddition.findByMpcaAdditionTypeAddId", query = "SELECT m FROM MpcaProductAddition m WHERE m.mpcaProductAdditionPK.mpcaAdditionTypeAddId = :mpcaAdditionTypeAddId"),
    @NamedQuery(name = "MpcaProductAddition.findByValue", query = "SELECT m FROM MpcaProductAddition m WHERE m.value = :value")})
public class MpcaProductAddition implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MpcaProductAdditionPK mpcaProductAdditionPK;
    @Column(name = "VALUE")
    private String value;
    @JoinColumn(name = "MPCA_PRODUCT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaProduct mpcaProduct;
    @JoinColumn(name = "MPCA_ADDITION_TYPE_ADD_ID", referencedColumnName = "ADD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaAdditionType mpcaAdditionType;

    public MpcaProductAddition() {
    }

    public MpcaProductAddition(MpcaProductAdditionPK mpcaProductAdditionPK) {
        this.mpcaProductAdditionPK = mpcaProductAdditionPK;
    }

    public MpcaProductAddition(long mpcaProductProductId, long mpcaAdditionTypeAddId) {
        this.mpcaProductAdditionPK = new MpcaProductAdditionPK(mpcaProductProductId, mpcaAdditionTypeAddId);
    }

    public MpcaProductAdditionPK getMpcaProductAdditionPK() {
        return mpcaProductAdditionPK;
    }

    public void setMpcaProductAdditionPK(MpcaProductAdditionPK mpcaProductAdditionPK) {
        this.mpcaProductAdditionPK = mpcaProductAdditionPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MpcaProduct getMpcaProduct() {
        return mpcaProduct;
    }

    public void setMpcaProduct(MpcaProduct mpcaProduct) {
        this.mpcaProduct = mpcaProduct;
    }

    public MpcaAdditionType getMpcaAdditionType() {
        return mpcaAdditionType;
    }

    public void setMpcaAdditionType(MpcaAdditionType mpcaAdditionType) {
        this.mpcaAdditionType = mpcaAdditionType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpcaProductAdditionPK != null ? mpcaProductAdditionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductAddition)) {
            return false;
        }
        MpcaProductAddition other = (MpcaProductAddition) object;
        if ((this.mpcaProductAdditionPK == null && other.mpcaProductAdditionPK != null) || (this.mpcaProductAdditionPK != null && !this.mpcaProductAdditionPK.equals(other.mpcaProductAdditionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductAddition[ mpcaProductAdditionPK=" + mpcaProductAdditionPK + " ]";
    }
    
}
