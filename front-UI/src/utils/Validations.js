import * as Yup from "yup";

export const logInSchema = Yup.object().shape({
  login: Yup.string()
    .min(2, "login_length_min")
    .max(50, "login_length_max")
    .required("login_required"),
  password: Yup.string()
    .min(8, "password_length_min")
    .max(50, "password_length_max")
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
      "password_invalid"
    )
    .required("password_required"),
});

export const signUpSchema = Yup.object().shape({
  name: Yup.string()
    .min(2, "first_name_length_min")
    .max(50, "first_name_length_max")
    .required("first_name_required"),
  lastName: Yup.string()
    .min(2, "last_name_lenght_min")
    .max(50, "last_name_length_max")
    .required("last_name_required"),
  login: Yup.string()
    .min(5, "login_length_min")
    .max(50, "login_length_max")
    .required("login_required"),
  email: Yup.string().email("email_valid").required("email_required"),
  password: Yup.string()
    .min(8, "password_length_min")
    .max(50, "password_length_max")
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
      "password_invalid"
    )
    .required("Password is required"),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref("password"), null], "passwords_not_match")
    .required("confirm_password_required"),
  phoneNumber: Yup.string()
    .min(9, "phone_number_length")
    .max(9, "phone_number_length")
    .matches(/^[0-9]+$/, "phone_number_only_digits")
    .required("phone_number_required"),
  pesel: Yup.string()
    .min(11, "pesel_length")
    .max(11, "pesel_length")
    .matches(/^[0-9]+$/, "pesel_only_digits")
    .required("pesel_required"),
  nip: Yup.string()
    .min(10, "nip_length")
    .matches(/^[0-9]+$/, "nip_only_digits")
    .max(10, "nip_length")
    .required("nip_required"),
});

export const adminSchema = Yup.object().shape({
  workPhoneNumber: Yup.string()
    .min(9, "phone_number_length")
    .max(9, "phone_number_length")
    .matches(/^[0-9]+$/, "phone_number_only_digits")
    .required("phone_number_required"),
});

export const chemistSchema = Yup.object().shape({
  licenseNumber: Yup.string()
    .min(6, "license_number_length")
    .max(6, "license_number_length")
    .matches(/^[0-9]+$/, "license_number_only_digits")
    .required("license_number_required"),
});

export const patientSchema = Yup.object().shape({
  name: Yup.string()
    .min(2, "first_name_length_min")
    .max(50, "first_name_length_max")
    .required("first_name_required"),
  lastName: Yup.string()
    .min(2, "last_name_lenght_min")
    .max(50, "last_name_length_max")
    .required("last_name_required"),
  phoneNumber: Yup.string()
    .min(9, "phone_number_length")
    .max(9, "phone_number_length")
    .matches(/^[0-9]+$/, "phone_number_only_digits")
    .required("phone_number_required"),
  pesel: Yup.string()
    .min(11, "pesel_length")
    .max(11, "pesel_length")
    .matches(/^[0-9]+$/, "pesel_only_digits")
    .required("pesel_required"),
  nip: Yup.string()
    .min(10, "nip_length")
    .matches(/^[0-9]+$/, "nip_only_digits")
    .max(10, "nip_length")
    .required("nip_required"),
});

export const medicationSchema = Yup.object().shape({
  name: Yup.string()
    .min(2, "name_length_min")
    .max(50, "name_length_max")
    .required("name_required"),
  stock: Yup.number()
    .min(1, "stock_min")
    .max(1000000, "stock_max")
    .required("stock_required"),
  price: Yup.number()
    .min(0.01, "price_min")
    .max(1000000, "price_max")
    .required("price_required"),
  categoryName: Yup.string()
    .min(2, "category_name_length_min")
    .max(50, "category_name_length_max")
    .required("category_name_required"),
});

export const categorySchema = Yup.object().shape({
  name: Yup.string()
    .required("name_required")
    .min(3, "name_min")
    .max(50, "name_max"),
  isOnPrescription: Yup.boolean().required("isOnPrescription_required"),
});
