/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
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
    @NamedQuery(name = "MpcaProductIndex.findByIndexValue", query = "SELECT m FROM MpcaProductIndex m WHERE m.indexValue = :indexValue"),
    @NamedQuery(name = "MpcaProductIndex.findByProductIndexId", query = "SELECT m FROM MpcaProductIndex m WHERE m.productIndexId = :productIndexId"),
    @NamedQuery(name = "MpcaProductIndex.findByProductAndIndexTpeAndLabel", query = "SELECT m FROM MpcaProductIndex m WHERE m.mpcaProductProductId.productId = :productId AND m.mpcaIndexTypeIndexId.indexId = :indexId AND m.labelId.labelId = :labelId"),
    @NamedQuery(name = "MpcaProductIndex.findByProductAndIndexTpe", query = "SELECT m FROM MpcaProductIndex m WHERE m.mpcaProductProductId.productId = :productId AND m.mpcaIndexTypeIndexId.indexId = :indexId")
})
public class MpcaProductIndex implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "INDEX_VALUE")
    private BigDecimal indexValue;
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_INDEX_ID")
    @SequenceGenerator(name="SEQ_PRODUCT_INDEX", sequenceName = "MPCA_PRODUCT_INDEX_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PRODUCT_INDEX")
    private Long productIndexId;
    @JoinColumn(name = "MPCA_PRODUCT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaProduct mpcaProductProductId;
    @JoinColumn(name = "LABEL_ID", referencedColumnName = "LABEL_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaLabelType labelId;
    @JoinColumn(name = "MPCA_INDEX_TYPE_INDEX_ID", referencedColumnName = "INDEX_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaIndexType mpcaIndexTypeIndexId;

    public MpcaProductIndex() {
    }

    public MpcaProductIndex(Long productIndexId) {
        this.productIndexId = productIndexId;
    }

    public MpcaProductIndex(Long productIndexId, BigDecimal indexValue) {
        this.productIndexId = productIndexId;
        this.indexValue = indexValue;
    }

    public BigDecimal getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(BigDecimal indexValue) {
        this.indexValue = indexValue;
    }

    public Long getProductIndexId() {
        return productIndexId;
    }

    public void setProductIndexId(Long productIndexId) {
        this.productIndexId = productIndexId;
    }

    public MpcaProduct getMpcaProductProductId() {
        return mpcaProductProductId;
    }

    public void setMpcaProductProductId(MpcaProduct mpcaProductProductId) {
        this.mpcaProductProductId = mpcaProductProductId;
    }

    public MpcaLabelType getLabelId() {
        return labelId;
    }

    public void setLabelId(MpcaLabelType labelId) {
        this.labelId = labelId;
    }

    public MpcaIndexType getMpcaIndexTypeIndexId() {
        return mpcaIndexTypeIndexId;
    }

    public void setMpcaIndexTypeIndexId(MpcaIndexType mpcaIndexTypeIndexId) {
        this.mpcaIndexTypeIndexId = mpcaIndexTypeIndexId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productIndexId != null ? productIndexId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductIndex)) {
            return false;
        }
        MpcaProductIndex other = (MpcaProductIndex) object;
        if ((this.productIndexId == null && other.productIndexId != null) || (this.productIndexId != null && !this.productIndexId.equals(other.productIndexId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductIndex[ productIndexId=" + productIndexId + " ]";
    }
    
}
