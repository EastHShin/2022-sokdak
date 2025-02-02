import styled from '@emotion/styled';

export const Container = styled.div`
  width: 65px;
  height: 65px;
  border-radius: 100%;
  background-color: ${props => props.theme.colors.sub};
  color: white;
  filter: drop-shadow(0px 4px 4px rgba(0, 0, 0, 0.25));

  display: flex;
  align-items: center;
  justify-content: center;

  font-weight: 500;
  font-size: 30px;
  user-select: none;
  cursor: pointer;
  position: fixed;
  bottom: 20px;
  right: calc((100% - 325px) / 2);
`;
