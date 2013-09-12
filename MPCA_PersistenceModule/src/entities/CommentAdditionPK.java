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
public class CommentAdditionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "COMMENT_COMMENT_ID")
    private BigInteger commentCommentId;
    @Basic(optional = false)
    @Column(name = "COMMENT_PAGE_ID")
    private BigInteger commentPageId;
    @Basic(optional = false)
    @Column(name = "COMMENT_PRODUCT_ID")
    private BigInteger commentProductId;
    @Basic(optional = false)
    @Column(name = "COMMENT_BRAND_ID")
    private BigInteger commentBrandId;
    @Basic(optional = false)
    @Column(name = "COMMENT_AUTHOR_ID")
    private BigInteger commentAuthorId;
    @Basic(optional = false)
    @Column(name = "ADDITION_ADD_ID")
    private BigInteger additionAddId;

    public CommentAdditionPK() {
    }

    public CommentAdditionPK(BigInteger commentCommentId, BigInteger commentPageId, BigInteger commentProductId, BigInteger commentBrandId, BigInteger commentAuthorId, BigInteger additionAddId) {
        this.commentCommentId = commentCommentId;
        this.commentPageId = commentPageId;
        this.commentProductId = commentProductId;
        this.commentBrandId = commentBrandId;
        this.commentAuthorId = commentAuthorId;
        this.additionAddId = additionAddId;
    }

    public BigInteger getCommentCommentId() {
        return commentCommentId;
    }

    public void setCommentCommentId(BigInteger commentCommentId) {
        this.commentCommentId = commentCommentId;
    }

    public BigInteger getCommentPageId() {
        return commentPageId;
    }

    public void setCommentPageId(BigInteger commentPageId) {
        this.commentPageId = commentPageId;
    }

    public BigInteger getCommentProductId() {
        return commentProductId;
    }

    public void setCommentProductId(BigInteger commentProductId) {
        this.commentProductId = commentProductId;
    }

    public BigInteger getCommentBrandId() {
        return commentBrandId;
    }

    public void setCommentBrandId(BigInteger commentBrandId) {
        this.commentBrandId = commentBrandId;
    }

    public BigInteger getCommentAuthorId() {
        return commentAuthorId;
    }

    public void setCommentAuthorId(BigInteger commentAuthorId) {
        this.commentAuthorId = commentAuthorId;
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
        hash += (commentCommentId != null ? commentCommentId.hashCode() : 0);
        hash += (commentPageId != null ? commentPageId.hashCode() : 0);
        hash += (commentProductId != null ? commentProductId.hashCode() : 0);
        hash += (commentBrandId != null ? commentBrandId.hashCode() : 0);
        hash += (commentAuthorId != null ? commentAuthorId.hashCode() : 0);
        hash += (additionAddId != null ? additionAddId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommentAdditionPK)) {
            return false;
        }
        CommentAdditionPK other = (CommentAdditionPK) object;
        if ((this.commentCommentId == null && other.commentCommentId != null) || (this.commentCommentId != null && !this.commentCommentId.equals(other.commentCommentId))) {
            return false;
        }
        if ((this.commentPageId == null && other.commentPageId != null) || (this.commentPageId != null && !this.commentPageId.equals(other.commentPageId))) {
            return false;
        }
        if ((this.commentProductId == null && other.commentProductId != null) || (this.commentProductId != null && !this.commentProductId.equals(other.commentProductId))) {
            return false;
        }
        if ((this.commentBrandId == null && other.commentBrandId != null) || (this.commentBrandId != null && !this.commentBrandId.equals(other.commentBrandId))) {
            return false;
        }
        if ((this.commentAuthorId == null && other.commentAuthorId != null) || (this.commentAuthorId != null && !this.commentAuthorId.equals(other.commentAuthorId))) {
            return false;
        }
        if ((this.additionAddId == null && other.additionAddId != null) || (this.additionAddId != null && !this.additionAddId.equals(other.additionAddId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CommentAdditionPK[ commentCommentId=" + commentCommentId + ", commentPageId=" + commentPageId + ", commentProductId=" + commentProductId + ", commentBrandId=" + commentBrandId + ", commentAuthorId=" + commentAuthorId + ", additionAddId=" + additionAddId + " ]";
    }
    
}
