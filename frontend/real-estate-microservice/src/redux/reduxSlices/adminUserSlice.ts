import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { AdminPermissions } from '../../commonTypes';

interface UserState {
  avatar: string;
  createdAt: string;
  deleted: boolean;
  email: string;
  fullName: string;
  permissions: any;
  phone: string;
  postList: string[],
  favoritePosts: string[],
  role_id: string;
  status: string;
  updatedAt: string;
  _id: string;
}

const initialState: UserState = {
  avatar: '',
  createdAt: '',
  deleted: false,
  email: '',
  fullName: '',
  permissions: undefined,
  phone: '',
  role_id: '',
  postList: [],
  favoritePosts: [],
  status: '',
  updatedAt: '',
  _id: '',
};

export const adminUserSlice = createSlice({
  name: 'adminUser',
  initialState,
  reducers: {

    setAdminUser: (_, action: PayloadAction<UserState>) => {
      return action.payload;
    },

    setPermissions: (state, action: PayloadAction<AdminPermissions | undefined>) => {
      state.permissions = action.payload;
    },

    setAvatar: (state, action: PayloadAction<string>) => {
      state.avatar = action.payload;
    },

    resetAdminUserState: state => {
      return initialState;
    }
  },
});

export const { setAdminUser, setPermissions, setAvatar, resetAdminUserState } = adminUserSlice.actions;

export default adminUserSlice.reducer;
