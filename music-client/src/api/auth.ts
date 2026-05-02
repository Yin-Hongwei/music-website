import { api, type ApiResponse } from "@/utils/request";
import { jsonRequestConfig } from "@/api/types";

export interface SignUpPayload {
  username: string;
  password: string;
  sex: number;
  phoneNum: string;
  email: string;
  birth: Date;
  introduction: string;
  location: string;
}

export interface SignInPayload {
  username: string;
  password: string;
}

function formatBirthForBackend(value: Date): string {
  const year = value.getFullYear();
  const month = `${value.getMonth() + 1}`.padStart(2, "0");
  const day = `${value.getDate()}`.padStart(2, "0");
  return `${year}-${month}-${day} 00:00:00`;
}

const signUpFallback: ApiResponse<null> = {
  code: 500,
  success: false,
  message: "注册失败，请稍后重试",
  data: null,
};

const signInFallback: ApiResponse<null> = {
  code: 500,
  success: false,
  message: "登录失败，请稍后重试",
  data: null,
};

export async function fetchSignUp(payload: SignUpPayload): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/add",
      data: {
        ...payload,
        birth: formatBirthForBackend(payload.birth),
      },
      config: jsonRequestConfig,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || signUpFallback.message;
    return {
      ...signUpFallback,
      message,
    };
  }
}

export async function fetchSignIn(payload: SignInPayload): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/login/status",
      data: payload,
      config: jsonRequestConfig,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || signInFallback.message;
    return {
      ...signInFallback,
      message,
    };
  }
}

export type SendPasswordResetCodeData = { debugCode?: string } | null;

export async function fetchSendPasswordResetCode(
  email: string,
): Promise<ApiResponse<SendPasswordResetCodeData>> {
  try {
    return await api<SendPasswordResetCodeData>({
      method: "get",
      url: "user/sendVerificationCode",
      config: { params: { email } },
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || "发送失败，请稍后重试";
    return { code: 500, success: false, message, data: null };
  }
}

export interface ResetPasswordPayload {
  email: string;
  code: string;
  password: string;
  confirmPassword: string;
}

const resetPasswordFallback: ApiResponse<null> = {
  code: 500,
  success: false,
  message: "重置失败，请稍后重试",
  data: null,
};

export async function fetchResetPassword(payload: ResetPasswordPayload): Promise<ApiResponse> {
  try {
    return await api({
      method: "post",
      url: "user/resetPassword",
      data: payload,
      config: jsonRequestConfig,
    });
  } catch (error: any) {
    const message =
      error?.data?.message || error?.response?.data?.message || resetPasswordFallback.message;
    return {
      ...resetPasswordFallback,
      message,
    };
  }
}
