import {
  Box,
  Button,
  FormControl,
  FormErrorMessage,
  Input,
  InputGroup,
  InputRightElement,
  Text,
} from "@chakra-ui/react";
import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import Router from "next/router";
import { ExerciseFormData } from "../../types";
import { postPrivateData } from "../../utils/NetworkUtils";

function PL4Form() {
  const validationSchema = Yup.object().shape({
    code: Yup.string().required("Exercise code is required"),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ExerciseFormData>({
    mode: "onBlur",
    resolver: yupResolver(validationSchema),
  });

  const onSubmit = (data: ExerciseFormData) => {
    postPL4(data);
    reset();
    Router.replace(Router.asPath);
  };

  return (
    <Box>
      <Text>Enter a PL4 code:</Text>
      <form autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
        <FormControl isInvalid={!!errors?.code?.message} isRequired>
          <InputGroup>
            <Input
              variant="flushed"
              id="code-field"
              type="text"
              {...register("code")}
              placeholder="TBD"
            />
            <InputRightElement width="4.5rem">
              <Button h="1.75rem" size="sm" type="submit">
                Submit
              </Button>
            </InputRightElement>
          </InputGroup>
          <FormErrorMessage>{errors?.code?.message}</FormErrorMessage>
        </FormControl>
      </form>
    </Box>
  );
}

const postPL4 = async (data: ExerciseFormData) => {
  await postPrivateData<ExerciseFormData>("/api/admin/exercises/PL4", data);
};

export default PL4Form;
