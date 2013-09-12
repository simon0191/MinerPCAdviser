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
public class ProductPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private BigInteger productId;
    @Basic(optional = false)
    @Column(name = "BRAND_ID")
    private BigInteger brandId;

    public ProductPK() {
    }

    public ProductPK(BigInteger productId, BigInteger brandId) {
        this.productId = productId;
        this.brandId = brandId;
    }

    public BigInteger getProductId() {
        return productId;
    }

    public void setProductId(BigInteger productId) {
        this.productId = productId;
    }

    public BigInteger getBrandId() {
        return brandId;
    }

    public void setBrandId(BigInteger brandId) {
        this.brandId = brandId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        hash += (brandId != null ? brandId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductPK)) {
            return false;
        }
        ProductPK other = (ProductPK) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        if ((this.brandId == null && other.brandId != null) || (this.brandId != null && !this.brandId.equals(other.brandId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProductPK[ productId=" + productId + ", brandId=" + brandId + " ]";
    }
    
}
