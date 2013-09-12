/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "PRODUCT_WEB_PAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductWebPage.findAll", query = "SELECT p FROM ProductWebPage p"),
    @NamedQuery(name = "ProductWebPage.findByProductProductId", query = "SELECT p FROM ProductWebPage p WHERE p.productWebPagePK.productProductId = :productProductId"),
    @NamedQuery(name = "ProductWebPage.findByProductBrandId", query = "SELECT p FROM ProductWebPage p WHERE p.productWebPagePK.productBrandId = :productBrandId"),
    @NamedQuery(name = "ProductWebPage.findByWebPagePageId", query = "SELECT p FROM ProductWebPage p WHERE p.productWebPagePK.webPagePageId = :webPagePageId"),
    @NamedQuery(name = "ProductWebPage.findByProductUrl", query = "SELECT p FROM ProductWebPage p WHERE p.productUrl = :productUrl"),
    @NamedQuery(name = "ProductWebPage.findByCommentUrl", query = "SELECT p FROM ProductWebPage p WHERE p.commentUrl = :commentUrl"),
    @NamedQuery(name = "ProductWebPage.findByLastUpdate", query = "SELECT p FROM ProductWebPage p WHERE p.lastUpdate = :lastUpdate")})
public class ProductWebPage implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductWebPagePK productWebPagePK;
    @Basic(optional = false)
    @Column(name = "PRODUCT_URL")
    private String productUrl;
    @Basic(optional = false)
    @Column(name = "COMMENT_URL")
    private String commentUrl;
    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @JoinColumn(name = "WEB_PAGE_PAGE_ID", referencedColumnName = "PAGE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WebPage webPage;
    @JoinColumns({
        @JoinColumn(name = "PRODUCT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false),
        @JoinColumn(name = "PRODUCT_BRAND_ID", referencedColumnName = "BRAND_ID", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;

    public ProductWebPage() {
    }

    public ProductWebPage(ProductWebPagePK productWebPagePK) {
        this.productWebPagePK = productWebPagePK;
    }

    public ProductWebPage(ProductWebPagePK productWebPagePK, String productUrl, String commentUrl, Date lastUpdate) {
        this.productWebPagePK = productWebPagePK;
        this.productUrl = productUrl;
        this.commentUrl = commentUrl;
        this.lastUpdate = lastUpdate;
    }

    public ProductWebPage(BigInteger productProductId, BigInteger productBrandId, BigInteger webPagePageId) {
        this.productWebPagePK = new ProductWebPagePK(productProductId, productBrandId, webPagePageId);
    }

    public ProductWebPagePK getProductWebPagePK() {
        return productWebPagePK;
    }

    public void setProductWebPagePK(ProductWebPagePK productWebPagePK) {
        this.productWebPagePK = productWebPagePK;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productWebPagePK != null ? productWebPagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductWebPage)) {
            return false;
        }
        ProductWebPage other = (ProductWebPage) object;
        if ((this.productWebPagePK == null && other.productWebPagePK != null) || (this.productWebPagePK != null && !this.productWebPagePK.equals(other.productWebPagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProductWebPage[ productWebPagePK=" + productWebPagePK + " ]";
    }
    
}
