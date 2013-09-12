/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Antonio
 */
@Embeddable
public class ProductAdditionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "PRODUCT_PRODUCT_ID")
    private BigInteger productProductId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_BRAND_ID")
    private BigInteger productBrandId;
    @Basic(optional = false)
    @Column(name = "ADDITION_ADD_ID")
    private BigInteger additionAddId;

    public ProductAdditionPK() {
    }

    public ProductAdditionPK(BigInteger productProductId, BigInteger productBrandId, BigInteger additionAddId) {
        this.productProductId = productProductId;
        this.productBrandId = productBrandId;
        this.additionAddId = additionAddId;
    }

    public BigInteger getProductProductId() {
        return productProductId;
    }

    public void setProductProductId(BigInteger productProductId) {
        this.productProductId = productProductId;
    }

    public BigInteger getProductBrandId() {
        return productBrandId;
    }

    public void setProductBrandId(BigInteger productBrandId) {
        this.productBrandId = productBrandId;
    }

    public BigInteger getAdditionAddId() {
        return additionAddId;
    }

    public void setAdditionAddId(BigInteger additionAddId) {
        this.additionAddId = additionAddId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productProductId != null ? productProductId.hashCode() : 0);
        hash += (productBrandId != null ? productBrandId.hashCode() : 0);
        hash += (additionAddId != null ? additionAddId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductAdditionPK)) {
            return false;
        }
        ProductAdditionPK other = (ProductAdditionPK) object;
        if ((this.productProductId == null && other.productProductId != null) || (this.productProductId != null && !this.productProductId.equals(other.productProductId))) {
            return false;
        }
        if ((this.productBrandId == null && other.productBrandId != null) || (this.productBrandId != null && !this.productBrandId.equals(other.productBrandId))) {
            return false;
        }
        if ((this.additionAddId == null && other.additionAddId != null) || (this.additionAddId != null && !this.additionAddId.equals(other.additionAddId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProductAdditionPK[ productProductId=" + productProductId + ", productBrandId=" + productBrandId + ", additionAddId=" + additionAddId + " ]";
    }
    
}
