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
public class ProductWebPagePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "PRODUCT_PRODUCT_ID")
    private BigInteger productProductId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_BRAND_ID")
    private BigInteger productBrandId;
    @Basic(optional = false)
    @Column(name = "WEB_PAGE_PAGE_ID")
    private BigInteger webPagePageId;

    public ProductWebPagePK() {
    }

    public ProductWebPagePK(BigInteger productProductId, BigInteger productBrandId, BigInteger webPagePageId) {
        this.productProductId = productProductId;
        this.productBrandId = productBrandId;
        this.webPagePageId = webPagePageId;
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

    public BigInteger getWebPagePageId() {
        return webPagePageId;
    }

    public void setWebPagePageId(BigInteger webPagePageId) {
        this.webPagePageId = webPagePageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productProductId != null ? productProductId.hashCode() : 0);
        hash += (productBrandId != null ? productBrandId.hashCode() : 0);
        hash += (webPagePageId != null ? webPagePageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductWebPagePK)) {
            return false;
        }
        ProductWebPagePK other = (ProductWebPagePK) object;
        if ((this.productProductId == null && other.productProductId != null) || (this.productProductId != null && !this.productProductId.equals(other.productProductId))) {
            return false;
        }
        if ((this.productBrandId == null && other.productBrandId != null) || (this.productBrandId != null && !this.productBrandId.equals(other.productBrandId))) {
            return false;
        }
        if ((this.webPagePageId == null && other.webPagePageId != null) || (this.webPagePageId != null && !this.webPagePageId.equals(other.webPagePageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProductWebPagePK[ productProductId=" + productProductId + ", productBrandId=" + productBrandId + ", webPagePageId=" + webPagePageId + " ]";
    }
    
}
