import { useReducer } from 'react';

import ConfirmModal from '@/components/ConfirmModal';

import useDeleteComment from '@/hooks/queries/comment/useDeleteComment';
import useReportComment from '@/hooks/queries/comment/useReportComment';

import * as Styled from './index.styles';

import timeConverter from '@/utils/timeConverter';

import ReplyForm from '../ReplyForm';
import ReportModal from '../ReportModal';

const Mode = {
  COMMENTS: 'comments',
  REPLIES: 'replies',
} as const;

interface CommentBoxProps extends CommentType {
  blocked: boolean;
  postWriter: boolean;
  mode?: typeof Mode[keyof typeof Mode];
  className?: string;
}

const CommentBox = ({
  id,
  nickname,
  content,
  createdAt,
  authorized,
  blocked,
  postWriter,
  mode = Mode.COMMENTS,
  className,
}: CommentBoxProps) => {
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, false);
  const [isDeleteModalOpen, handleDeleteModal] = useReducer(state => !state, false);
  const [isReplyFormOpen, handleReplyForm] = useReducer(state => !state, false);

  const { mutate: deleteComment } = useDeleteComment();
  const { mutate: reportComment } = useReportComment({
    onSettled: () => {
      handleReportModal();
    },
  });

  const handleClickReportButton = () => {
    handleReportModal();
  };

  const handleClickDeleteButton = () => {
    handleDeleteModal();
  };

  const submitReportComment = (message: string) => {
    reportComment({ id, message });
  };

  if (!content) {
    return <Styled.EmptyComment>작성자에 의해 삭제된 댓글 입니다.</Styled.EmptyComment>;
  }

  if (blocked) {
    return <Styled.EmptyComment>신고에 의해 블라인드 처리되었습니다.</Styled.EmptyComment>;
  }

  return (
    <>
      <Styled.Container className={className}>
        <Styled.CommentHeader>
          <Styled.Nickname>
            {nickname} {postWriter && <Styled.PostWriter>작성자</Styled.PostWriter>}
          </Styled.Nickname>
          <Styled.ButtonContainer>
            {mode === Mode.COMMENTS && <Styled.ReplyButton onClick={handleReplyForm}>답글</Styled.ReplyButton>}
            {authorized ? (
              <Styled.DeleteButton onClick={handleClickDeleteButton}>삭제</Styled.DeleteButton>
            ) : (
              <Styled.ReportButton onClick={handleClickReportButton}>🚨</Styled.ReportButton>
            )}
          </Styled.ButtonContainer>
        </Styled.CommentHeader>
        <Styled.Content>{content}</Styled.Content>
        <Styled.Date>{timeConverter(createdAt!)}</Styled.Date>
      </Styled.Container>

      {isReplyFormOpen && <ReplyForm commentId={id} />}
      {isDeleteModalOpen && (
        <ConfirmModal
          title="삭제"
          notice="해당 댓글을 삭제하시겠습니까?"
          handleCancel={handleClickDeleteButton}
          handleConfirm={() => deleteComment({ id })}
        />
      )}
      <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} submitReport={submitReportComment} />
    </>
  );
};

export default CommentBox;
