import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

type Limit = number;
type Include = string;

const useHashtags = ({
  storeCode,
  options,
}: {
  storeCode: [Limit, Include];
  options?: UseQueryOptions<
    AxiosResponse,
    AxiosError,
    AxiosResponse<{ hashtags: Hashtag[] }>,
    [QueryKey, Limit, Include]
  >;
}) => {
  return useQuery(
    [QUERY_KEYS.HASHTAGS, ...storeCode],
    ({ queryKey: [, limit, include] }) => axios.get(`/hashtags/popular?limit=${limit}&include=${include}`),
    {
      ...options,
    },
  );
};

export default useHashtags;
