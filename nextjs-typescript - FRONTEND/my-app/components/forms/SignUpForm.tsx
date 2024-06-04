import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import * as Yup from 'yup';
import { useAuth } from '../../contexts/auth';
import { LoginForm } from '../../pages/login';


function SignUpForm() {

    const validationSchema = Yup.object().shape({
        username: Yup.string().required('Username is required'),
        password: Yup.string().required('Password is required')
    });
    const formOptions = { resolver: yupResolver(validationSchema) };

    // get functions to build form with useForm() hook
    const { register, handleSubmit, setError, formState } = useForm<LoginForm>(formOptions);
    const { signUp } = useAuth()
    const { errors } = formState;

    const onSubmit = (data: LoginForm) => {
        console.log(JSON.stringify(data, null, 2));

        signUp(data)
    };


    return (
        // <FormControl>

        //     <FormLabel htmlFor='email'>Username</FormLabel>
        //     <Input id='email' type='email' />
        //     <FormHelperText>Enter username.</FormHelperText>

        //     <FormLabel htmlFor='email'>Password</FormLabel>
        //     <Input id='password' type='password' />
        //     <FormHelperText>Enter password.</FormHelperText>

        // </FormControl>



        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="form-group">
                <label>Username</label>
                <input
                    type="text"
                    {...register('username')}
                    className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                />
                <div className="invalid-feedback">{errors.username?.message}</div>
            </div>
            <div className="form-group">
                <label>Password</label>
                <input
                    type="password"
                    {...register('password')}
                    className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                />
                <div className="invalid-feedback">{errors.password?.message}</div>
            </div>
            <div className="form-group">
                <button type="submit" className="btn btn-primary">
                    Login
                </button>
            </div>
        </form>
    );
}

export default SignUpForm;