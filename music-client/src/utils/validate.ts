type ValidateCallback = (error?: Error) => void;

export const validatePassword = (
  _rule: unknown,
  value: string,
  callback: ValidateCallback,
) => {
  if (!value || !value.trim()) {
    callback(new Error("密码不能为空"));
    return;
  }
  if (value.length < 6) {
    callback(new Error("密码至少 6 位"));
    return;
  }
  callback();
};
