import {
  Flex,
  Heading,
  Input,
  Button,
  InputGroup,
  Stack,
  InputLeftElement,
  chakra,
  Box,
  FormControl,
  InputRightElement,
  FormErrorMessage,
  Spinner,
} from "@chakra-ui/react";

import { FaUserAlt, FaLock } from "react-icons/fa";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/router";

import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";

import { useAuth } from "../../contexts/auth";
import Head from "next/head";

const CFaUserAlt = chakra(FaUserAlt);
const CFaLock = chakra(FaLock);

export interface LoginForm {
  username: string;
  password: string;
}

const Login = () => {
  const { user, requestLogin, loading } = useAuth();
  const [showPassword, setShowPassword] = useState(false);

  const isAuthenticated = !!user;

  const router = useRouter();

  const validationSchema = Yup.object().shape({
    username: Yup.string().required("Username is required"),
    password: Yup.string().required("Password is required"),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginForm>({
    mode: "onBlur",
    resolver: yupResolver(validationSchema),
  });

  const onSubmit = async (data: LoginForm) => {
    requestLogin(data);
  };

  const handleShowClick = () => setShowPassword(!showPassword);

  useEffect(() => {
    if (isAuthenticated) {
      console.log("There is already a logged in user");
      router.push("/");
    }
  }, []);

  if (loading || isAuthenticated) {
    return (
      <Flex height="100vh" justifyContent="center" alignItems="center">
        <Spinner />
      </Flex>
    );
  }

  return (
    <Box>
      <Head>
        <title>Login</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Flex
        flexDirection="column"
        width="100wh"
        height="90vh"
        backgroundColor="gray.200"
        justifyContent="center"
        alignItems="center"
      >
        <Stack
          flexDir="column"
          mb="2"
          justifyContent="center"
          alignItems="center"
        >
          <Heading color="#0096FF">Login</Heading>
          <Box minW={{ base: "90%", md: "468px" }}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack
                spacing={4}
                p="1rem"
                backgroundColor="whiteAlpha.900"
                boxShadow="md"
              >
                <FormControl isInvalid={!!errors?.username?.message} isRequired>
                  <InputGroup>
                    <InputLeftElement
                      pointerEvents="none"
                      children={<CFaUserAlt color="gray.300" />}
                    />
                    <Input
                      id="username-field"
                      type="text"
                      {...register("username")}
                      placeholder="Username"
                    />
                  </InputGroup>
                  <FormErrorMessage>
                    {errors?.username?.message}
                  </FormErrorMessage>
                </FormControl>
                <FormControl isInvalid={!!errors?.password?.message} isRequired>
                  <InputGroup>
                    <InputLeftElement
                      pointerEvents="none"
                      color="gray.300"
                      children={<CFaLock color="gray.300" />}
                    />
                    <Input
                      id="password-field"
                      type={showPassword ? "text" : "password"}
                      {...register("password")}
                      placeholder="Password"
                    />
                    <InputRightElement width="4.5rem">
                      <Button h="1.75rem" size="sm" onClick={handleShowClick}>
                        {showPassword ? "Hide" : "Show"}
                      </Button>
                    </InputRightElement>
                  </InputGroup>
                  <FormErrorMessage>
                    {errors?.password?.message}
                  </FormErrorMessage>
                </FormControl>
                <Button
                  borderRadius={0}
                  type="submit"
                  variant="solid"
                  colorScheme="blue"
                  width="full"
                >
                  Login
                </Button>
              </Stack>
            </form>
          </Box>
        </Stack>
      </Flex>
    </Box>
  );
};

export default Login;