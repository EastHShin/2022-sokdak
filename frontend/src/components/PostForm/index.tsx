import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

import HashTagInput from './components/HashTagInput';
import ImagePreview from './components/ImagePreview';
import ImageUpload from './components/ImageUpload';

import useUploadImage from '@/hooks/queries/post/useUploadImage';
import useSnackbar from '@/hooks/useSnackbar';

import * as Styled from './index.styles';

import { BOARDS } from '@/constants/board';

const SubmitType = {
  POST: '글 작성하기',
  PUT: '글 수정하기',
} as const;

interface PostFormProps {
  heading: string;
  submitType: typeof SubmitType[keyof typeof SubmitType];
  prevTitle?: string;
  prevContent?: string;
  prevHashTags?: Omit<Hashtag, 'count'>[];
  prevImagePath?: string;
  handlePost: (
    post: Pick<Post, 'title' | 'content' | 'imageName'> & {
      hashtags: string[];
      anonymous?: boolean;
      boardId: string | number;
    },
  ) => void;
}

const PostForm = ({
  heading,
  submitType,
  prevTitle = '',
  prevContent = '',
  prevHashTags = [],
  prevImagePath = '',
  handlePost,
}: PostFormProps) => {
  const { isVisible, showSnackbar } = useSnackbar();

  const [title, setTitle] = useState(prevTitle);
  const [content, setContent] = useState(prevContent);
  const [hashtags, setHashtags] = useState(prevHashTags.map(hashtag => hashtag.name));
  const [anonymous, setAnonymous] = useState(true);
  const [image, setImage] = useState<Image>({
    path: prevImagePath,
  });

  const { boardId } = useLocation().state as Pick<Post, 'boardId'>;
  const { title: boardTitle } = BOARDS.find(board => board.id === Number(boardId))!;

  const [isValidTitle, setIsValidTitle] = useState(true);
  const [isValidContent, setIsValidContent] = useState(true);
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isContentAnimationActive, setIsContentAnimationActive] = useState(false);

  const { mutateAsync: uploadImage, isLoading } = useUploadImage({});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    handlePost({ title, content, hashtags, anonymous, boardId, imageName: image.path });
  };

  return (
    <Styled.Container
      onSubmit={handleSubmit}
      onInvalid={(e: React.FormEvent<HTMLFormElement> & { target: HTMLInputElement }) => {
        if (isVisible) return;
        showSnackbar(e.target.placeholder);
      }}
    >
      <Styled.Heading>{heading}</Styled.Heading>
      <Styled.Board>{boardTitle}</Styled.Board>
      <Styled.TitleInput
        placeholder="제목을 입력해주세요."
        value={title}
        onChange={e => setTitle(e.target.value)}
        maxLength={50}
        onInvalid={e => {
          e.preventDefault();
          setIsValidTitle(false);
          setIsAnimationActive(true);
        }}
        onAnimationEnd={() => setIsAnimationActive(false)}
        isValid={isValidTitle}
        isAnimationActive={isAnimationActive}
        required
      />
      <Styled.ContentInput
        placeholder="내용을 작성해주세요."
        value={content}
        onChange={e => setContent(e.target.value)}
        maxLength={5000}
        onInvalid={e => {
          e.preventDefault();
          setIsValidContent(false);
          setIsContentAnimationActive(true);
        }}
        onAnimationEnd={() => setIsContentAnimationActive(false)}
        isValid={isValidContent}
        isAnimationActive={isContentAnimationActive}
        required
      />
      <ImagePreview image={image} isLoading={isLoading} />
      <HashTagInput hashtags={hashtags} setHashtags={setHashtags} />
      <Styled.SubmitButton>{submitType}</Styled.SubmitButton>
      <Styled.PostController>
        <Styled.CheckBox
          isChecked={anonymous}
          setIsChecked={setAnonymous}
          labelText="익명"
          visible={submitType === SubmitType.POST}
        />
        <ImageUpload setImage={setImage} uploadImage={uploadImage} />
      </Styled.PostController>
    </Styled.Container>
  );
};

export default PostForm;
