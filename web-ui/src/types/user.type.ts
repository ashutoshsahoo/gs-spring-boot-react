export default interface IUser {
  id?: any | null;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  roles?: Array<string>;
}
