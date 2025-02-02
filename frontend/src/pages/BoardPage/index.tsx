import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import BoardCategory from './components/BoardCategory';
import Layout from '@/components/@styled/Layout';
import FAB from '@/components/FAB';
import PostList from '@/components/PostList';
import Spinner from '@/components/Spinner';

import useBoards from '@/hooks/queries/board/useBoards';
import usePosts from '@/hooks/queries/post/usePosts';

import * as Styled from './index.styles';

import PATH from '@/constants/path';

const HOT_BOARD_ID = '1';

const BoardPage = () => {
  const navigate = useNavigate();
  const { id: boardId } = useParams();
  const { isLoading: boardIsLoading, data: boards } = useBoards({
    options: {
      staleTime: Infinity,
    },
  });
  const { isLoading, data, fetchNextPage, refetch } = usePosts({ storeCode: [boardId!, 3] });

  useEffect(() => {
    refetch();
  }, [boardId]);

  const handleClickFAB = () => {
    navigate(PATH.CREATE_POST, { state: { boardId } });
  };

  if (boardIsLoading || isLoading) {
    return (
      <Layout>
        <Styled.SpinnerContainer>
          <Spinner />
        </Styled.SpinnerContainer>
      </Layout>
    );
  }

  if (!boards?.some(({ id }) => id === Number(boardId))) {
    return <Layout></Layout>;
  }

  return (
    <Layout>
      <BoardCategory id={boardId!} boards={boards!} />
      <PostList data={data} fetchNextPage={fetchNextPage} />
      {boardId !== HOT_BOARD_ID && <FAB handleClick={handleClickFAB} />}
    </Layout>
  );
};

export default BoardPage;
