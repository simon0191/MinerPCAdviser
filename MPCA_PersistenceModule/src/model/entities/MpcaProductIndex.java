/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
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
@Table(name = "MPCA_PRODUCT_INDEX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaProductIndex.findAll", query = "SELECT m FROM MpcaProductIndex m"),
    @NamedQuery(name = "MpcaProductIndex.findByMpcaProductProductId", query = "SELECT m FROM MpcaProductIndex m WHERE m.mpcaProductIndexPK.mpcaProductProductId = :mpcaProductProductId"),
    @NamedQuery(name = "MpcaProductIndex.findByMpcaIndexTypeIndexId", query = "SELECT m FROM MpcaProductIndex m WHERE m.mpcaProductIndexPK.mpcaIndexTypeIndexId = :mpcaIndexTypeIndexId"),
    @NamedQuery(name = "MpcaProductIndex.findByIndexValue", query = "SELECT m FROM MpcaProductIndex m WHERE m.indexValue = :indexValue")})
public class MpcaProductIndex implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MpcaProductIndexPK mpcaProductIndexPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "INDEX_VALUE")
    private BigDecimal indexValue;
    @JoinColumn(name = "MPCA_PRODUCT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaProduct mpcaProduct;
    @JoinColumn(name = "LABEL_ID", referencedColumnName = "LABEL_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaLabelType labelId;
    @JoinColumn(name = "MPCA_INDEX_TYPE_INDEX_ID", referencedColumnName = "INDEX_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaIndexType mpcaIndexType;

    public MpcaProductIndex() {
    }

    public MpcaProductIndex(MpcaProductIndexPK mpcaProductIndexPK) {
        this.mpcaProductIndexPK = mpcaProductIndexPK;
    }

    public MpcaProductIndex(MpcaProductIndexPK mpcaProductIndexPK, BigDecimal indexValue) {
        this.mpcaProductIndexPK = mpcaProductIndexPK;
        this.indexValue = indexValue;
    }

    public MpcaProductIndex(long mpcaProductProductId, long mpcaIndexTypeIndexId) {
        this.mpcaProductIndexPK = new MpcaProductIndexPK(mpcaProductProductId, mpcaIndexTypeIndexId);
    }

    public MpcaProductIndexPK getMpcaProductIndexPK() {
        return mpcaProductIndexPK;
    }

    public void setMpcaProductIndexPK(MpcaProductIndexPK mpcaProductIndexPK) {
        this.mpcaProductIndexPK = mpcaProductIndexPK;
    }

    public BigDecimal getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(BigDecimal indexValue) {
        this.indexValue = indexValue;
    }

    public MpcaProduct getMpcaProduct() {
        return mpcaProduct;
    }

    public void setMpcaProduct(MpcaProduct mpcaProduct) {
        this.mpcaProduct = mpcaProduct;
    }

    public MpcaLabelType getLabelId() {
        return labelId;
    }

    public void setLabelId(MpcaLabelType labelId) {
        this.labelId = labelId;
    }

    public MpcaIndexType getMpcaIndexType() {
        return mpcaIndexType;
    }

    public void setMpcaIndexType(MpcaIndexType mpcaIndexType) {
        this.mpcaIndexType = mpcaIndexType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpcaProductIndexPK != null ? mpcaProductIndexPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductIndex)) {
            return false;
        }
        MpcaProductIndex other = (MpcaProductIndex) object;
        if ((this.mpcaProductIndexPK == null && other.mpcaProductIndexPK != null) || (this.mpcaProductIndexPK != null && !this.mpcaProductIndexPK.equals(other.mpcaProductIndexPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductIndex[ mpcaProductIndexPK=" + mpcaProductIndexPK + " ]";
    }
    
}
