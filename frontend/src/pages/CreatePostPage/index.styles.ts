import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const PostForm = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 0 13px;
  box-sizing: border-box;
`;

export const SpinnerContainer = styled.div`
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Heading = styled.h1`
  font-family: 'BMHANNAPro';
  font-size: 27px;
  margin: 40px 0;
`;

export const huduldul = keyframes`
  0%{
    transform:translateX(-10px)
  }20%{
    transform:translateX(10px)
  }40%{
    transform:translateX(-10px)
  }60%{
    transform:translateX(10px)
  }100%{
    transform:translateX(0px)
  }
`;

interface InputProps {
  isValid: boolean;
  isAnimationActive: boolean;
}

export const TitleInput = styled.input<InputProps>`
  font-family: 'BMHANNAAir';
  border-bottom: 1px solid ${props => (props.isValid ? props.theme.colors.sub : props.theme.colors.red_100)};

  width: 100%;
  padding: 10px;
  font-size: 20px;
  animation: ${props => (props.isAnimationActive ? huduldul : null)} 0.5s;

  :valid {
    border-bottom: 1px solid ${props => props.theme.colors.sub};
  }
`;

export const ContentInput = styled.textarea<InputProps>`
  width: 100%;
  height: 290px;
  padding: 10px;
  font-size: 14px;
  margin: 20px 0;
  animation: ${props => (props.isAnimationActive ? huduldul : null)} 0.5s;

  ::placeholder {
    color: ${props => (props.isValid ? 'gray' : props.theme.colors.red_100)};
  }

  :valid {
    ::placeholder {
      color: grey;
    }
  }
`;

export const PostButton = styled.button`
  font-family: 'BMHANNAAir';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100%;
  height: 55px;
  cursor: pointer;
`;
