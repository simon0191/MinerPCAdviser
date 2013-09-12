/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
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
@Table(name = "COMMENT_ADDITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommentAddition.findAll", query = "SELECT c FROM CommentAddition c"),
    @NamedQuery(name = "CommentAddition.findByCommentCommentId", query = "SELECT c FROM CommentAddition c WHERE c.commentAdditionPK.commentCommentId = :commentCommentId"),
    @NamedQuery(name = "CommentAddition.findByCommentPageId", query = "SELECT c FROM CommentAddition c WHERE c.commentAdditionPK.commentPageId = :commentPageId"),
    @NamedQuery(name = "CommentAddition.findByCommentProductId", query = "SELECT c FROM CommentAddition c WHERE c.commentAdditionPK.commentProductId = :commentProductId"),
    @NamedQuery(name = "CommentAddition.findByCommentBrandId", query = "SELECT c FROM CommentAddition c WHERE c.commentAdditionPK.commentBrandId = :commentBrandId"),
    @NamedQuery(name = "CommentAddition.findByCommentAuthorId", query = "SELECT c FROM CommentAddition c WHERE c.commentAdditionPK.commentAuthorId = :commentAuthorId"),
    @NamedQuery(name = "CommentAddition.findByAdditionAddId", query = "SELECT c FROM CommentAddition c WHERE c.commentAdditionPK.additionAddId = :additionAddId")})
public class CommentAddition implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommentAdditionPK commentAdditionPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "VALUE")
    private String value;
    @JoinColumns({
        @JoinColumn(name = "COMMENT_COMMENT_ID", referencedColumnName = "COMMENT_ID", insertable = false, updatable = false),
        @JoinColumn(name = "COMMENT_PAGE_ID", referencedColumnName = "PAGE_ID", insertable = false, updatable = false),
        @JoinColumn(name = "COMMENT_PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false),
        @JoinColumn(name = "COMMENT_BRAND_ID", referencedColumnName = "BRAND_ID", insertable = false, updatable = false),
        @JoinColumn(name = "COMMENT_AUTHOR_ID", referencedColumnName = "AUTHOR_ID", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaComment mpcaComment;
    @JoinColumn(name = "ADDITION_ADD_ID", referencedColumnName = "ADD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Addition addition;

    public CommentAddition() {
    }

    public CommentAddition(CommentAdditionPK commentAdditionPK) {
        this.commentAdditionPK = commentAdditionPK;
    }

    public CommentAddition(CommentAdditionPK commentAdditionPK, String value) {
        this.commentAdditionPK = commentAdditionPK;
        this.value = value;
    }

    public CommentAddition(BigInteger commentCommentId, BigInteger commentPageId, BigInteger commentProductId, BigInteger commentBrandId, BigInteger commentAuthorId, BigInteger additionAddId) {
        this.commentAdditionPK = new CommentAdditionPK(commentCommentId, commentPageId, commentProductId, commentBrandId, commentAuthorId, additionAddId);
    }

    public CommentAdditionPK getCommentAdditionPK() {
        return commentAdditionPK;
    }

    public void setCommentAdditionPK(CommentAdditionPK commentAdditionPK) {
        this.commentAdditionPK = commentAdditionPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MpcaComment getMpcaComment() {
        return mpcaComment;
    }

    public void setMpcaComment(MpcaComment mpcaComment) {
        this.mpcaComment = mpcaComment;
    }

    public Addition getAddition() {
        return addition;
    }

    public void setAddition(Addition addition) {
        this.addition = addition;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentAdditionPK != null ? commentAdditionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommentAddition)) {
            return false;
        }
        CommentAddition other = (CommentAddition) object;
        if ((this.commentAdditionPK == null && other.commentAdditionPK != null) || (this.commentAdditionPK != null && !this.commentAdditionPK.equals(other.commentAdditionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CommentAddition[ commentAdditionPK=" + commentAdditionPK + " ]";
    }
    
}
