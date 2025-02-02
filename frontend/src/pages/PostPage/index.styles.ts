import { Link } from 'react-router-dom';

import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const containerStyle = css`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const Container = styled.div`
  ${containerStyle}
`;

export const Image = styled.img`
  min-height: 300px;
  max-width: 100%;
  border-radius: 5px;
  margin-bottom: 50px;
  object-fit: scale-down;
`;

export const BlockContainer = styled.div`
  ${containerStyle}
  justify-content: center;
  height: calc(100vh - 166px);
  overflow: hidden;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  gap: 2em;
`;

export const BackButton = styled.button`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  padding: 1em;
  border-radius: 0.5em;
`;

export const ListButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
`;

export const ListButton = styled(Link)`
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 8px;
  font-size: 13px;
  padding: 4% 6%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const SpinnerContainer = styled.div`
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ErrorContainer = styled.div`
  width: 100%;
  height: 500px;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 3em;
`;
