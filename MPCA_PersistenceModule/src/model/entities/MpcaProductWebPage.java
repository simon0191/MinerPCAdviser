/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_PRODUCT_WEB_PAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaProductWebPage.findAll", query = "SELECT m FROM MpcaProductWebPage m"),
    @NamedQuery(name = "MpcaProductWebPage.findByMpcaProductProductId", query = "SELECT m FROM MpcaProductWebPage m WHERE m.mpcaProductWebPagePK.mpcaProductProductId = :mpcaProductProductId"),
    @NamedQuery(name = "MpcaProductWebPage.findByMpcaWebPagePageId", query = "SELECT m FROM MpcaProductWebPage m WHERE m.mpcaProductWebPagePK.mpcaWebPagePageId = :mpcaWebPagePageId"),
    @NamedQuery(name = "MpcaProductWebPage.findByProductUrl", query = "SELECT m FROM MpcaProductWebPage m WHERE m.productUrl = :productUrl"),
    @NamedQuery(name = "MpcaProductWebPage.findByCommentUrl", query = "SELECT m FROM MpcaProductWebPage m WHERE m.commentUrl = :commentUrl"),
    @NamedQuery(name = "MpcaProductWebPage.findByLastUpdate", query = "SELECT m FROM MpcaProductWebPage m WHERE m.lastUpdate = :lastUpdate")})
public class MpcaProductWebPage implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MpcaProductWebPagePK mpcaProductWebPagePK;
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
    @JoinColumn(name = "MPCA_WEB_PAGE_PAGE_ID", referencedColumnName = "PAGE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaWebPage mpcaWebPage;
    @JoinColumn(name = "MPCA_PRODUCT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaProduct mpcaProduct;

    public MpcaProductWebPage() {
    }

    public MpcaProductWebPage(MpcaProductWebPagePK mpcaProductWebPagePK) {
        this.mpcaProductWebPagePK = mpcaProductWebPagePK;
    }

    public MpcaProductWebPage(MpcaProductWebPagePK mpcaProductWebPagePK, String productUrl, String commentUrl, Date lastUpdate) {
        this.mpcaProductWebPagePK = mpcaProductWebPagePK;
        this.productUrl = productUrl;
        this.commentUrl = commentUrl;
        this.lastUpdate = lastUpdate;
    }

    public MpcaProductWebPage(long mpcaProductProductId, long mpcaWebPagePageId) {
        this.mpcaProductWebPagePK = new MpcaProductWebPagePK(mpcaProductProductId, mpcaWebPagePageId);
    }

    public MpcaProductWebPagePK getMpcaProductWebPagePK() {
        return mpcaProductWebPagePK;
    }

    public void setMpcaProductWebPagePK(MpcaProductWebPagePK mpcaProductWebPagePK) {
        this.mpcaProductWebPagePK = mpcaProductWebPagePK;
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

    public MpcaWebPage getMpcaWebPage() {
        return mpcaWebPage;
    }

    public void setMpcaWebPage(MpcaWebPage mpcaWebPage) {
        this.mpcaWebPage = mpcaWebPage;
    }

    public MpcaProduct getMpcaProduct() {
        return mpcaProduct;
    }

    public void setMpcaProduct(MpcaProduct mpcaProduct) {
        this.mpcaProduct = mpcaProduct;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpcaProductWebPagePK != null ? mpcaProductWebPagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaProductWebPage)) {
            return false;
        }
        MpcaProductWebPage other = (MpcaProductWebPage) object;
        if ((this.mpcaProductWebPagePK == null && other.mpcaProductWebPagePK != null) || (this.mpcaProductWebPagePK != null && !this.mpcaProductWebPagePK.equals(other.mpcaProductWebPagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaProductWebPage[ mpcaProductWebPagePK=" + mpcaProductWebPagePK + " ]";
    }
    
}
