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
public class MpcaCommentPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "COMMENT_ID")
    private BigInteger commentId;
    @Basic(optional = false)
    @Column(name = "PAGE_ID")
    private BigInteger pageId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private BigInteger productId;
    @Basic(optional = false)
    @Column(name = "AUTHOR_ID")
    private BigInteger authorId;
    @Basic(optional = false)
    @Column(name = "BRAND_ID")
    private BigInteger brandId;

    public MpcaCommentPK() {
    }

    public MpcaCommentPK(BigInteger commentId, BigInteger pageId, BigInteger productId, BigInteger authorId, BigInteger brandId) {
        this.commentId = commentId;
        this.pageId = pageId;
        this.productId = productId;
        this.authorId = authorId;
        this.brandId = brandId;
    }

    public BigInteger getCommentId() {
        return commentId;
    }

    public void setCommentId(BigInteger commentId) {
        this.commentId = commentId;
    }

    public BigInteger getPageId() {
        return pageId;
    }

    public void setPageId(BigInteger pageId) {
        this.pageId = pageId;
    }

    public BigInteger getProductId() {
        return productId;
    }

    public void setProductId(BigInteger productId) {
        this.productId = productId;
    }

    public BigInteger getAuthorId() {
        return authorId;
    }

    public void setAuthorId(BigInteger authorId) {
        this.authorId = authorId;
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
        hash += (commentId != null ? commentId.hashCode() : 0);
        hash += (pageId != null ? pageId.hashCode() : 0);
        hash += (productId != null ? productId.hashCode() : 0);
        hash += (authorId != null ? authorId.hashCode() : 0);
        hash += (brandId != null ? brandId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaCommentPK)) {
            return false;
        }
        MpcaCommentPK other = (MpcaCommentPK) object;
        if ((this.commentId == null && other.commentId != null) || (this.commentId != null && !this.commentId.equals(other.commentId))) {
            return false;
        }
        if ((this.pageId == null && other.pageId != null) || (this.pageId != null && !this.pageId.equals(other.pageId))) {
            return false;
        }
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        if ((this.authorId == null && other.authorId != null) || (this.authorId != null && !this.authorId.equals(other.authorId))) {
            return false;
        }
        if ((this.brandId == null && other.brandId != null) || (this.brandId != null && !this.brandId.equals(other.brandId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaCommentPK[ commentId=" + commentId + ", pageId=" + pageId + ", productId=" + productId + ", authorId=" + authorId + ", brandId=" + brandId + " ]";
    }
    
}
