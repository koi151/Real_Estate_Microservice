import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../stores';

interface ClientAccountState {
  userName: string;
  avatar: string;
  createdAt?: string;
  deleted: boolean;
  wallet?: number;
  email: string;
  fullName: string;
  phone: string;
  postList: any[];
  favoritePosts: any[];
  status?: string;
  updatedAt?: string;
  accountId: string;
  scope?: string;
}

const initialState: ClientAccountState = {
  userName: '',
  avatar: '',
  createdAt: undefined,
  deleted: false,
  wallet: undefined,
  email: '',
  fullName: '',
  phone: '',
  postList: [],
  favoritePosts: [],
  status: undefined,
  updatedAt: undefined,
  accountId: '',
  scope: undefined,
};

export const clientUserSlice = createSlice({
  name: 'clientUser',
  initialState,
  reducers: {
    setClientUser: (state, action: PayloadAction<Partial<ClientAccountState>>) => {
      return { ...state, ...action.payload };
    },
    setAvatar: (state, action: PayloadAction<string>) => {
      state.avatar = action.payload;
    },
    resetClientUserState: () => {
      return initialState;
    },
  },
});

export const { setClientUser, setAvatar, resetClientUserState } = clientUserSlice.actions;
export const selectClientUser = (state: RootState) => state.clientUser;

export default clientUserSlice.reducer;
