import styled from '@emotion/styled';

export const NotificationPageContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const Title = styled.h1`
  width: calc(100% - 1em);
  font-size: 2rem;
  font-family: 'BMHANNAPro';
  text-align: left;
  padding: 0.5em;
`;

export const NotificationContainer = styled.div`
  width: 100%;
  margin: 0.3em;
`;

export const NotificationTime = styled.h2`
  font-size: 1.2rem;
  font-family: 'BMHANNAPro';
  margin: 0.7em;
`;

export const NotificationItemContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5em;
`;
