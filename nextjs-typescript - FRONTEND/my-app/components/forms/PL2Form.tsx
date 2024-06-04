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
import { ExerciseFormData, ExerciseFormProps, PostExercise } from "../../types";
import { postPrivateDataVoid } from "../../utils/NetworkUtils";

function PL2Form({ user }: ExerciseFormProps) {
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

  const onSubmit = async (data: ExerciseFormData) => {
    const exercise: PostExercise = {
      code: data.code,
      instructor: user.username,
    };
    await postPL2(exercise);
    alert("Posted new PL2 Exercise");
    reset();
    Router.replace(Router.asPath);
  };

  return (
    <Box>
      <Text>Enter a PL2 code:</Text>
      <form autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
        <FormControl isInvalid={!!errors?.code?.message} isRequired>
          <InputGroup>
            <Input
              variant="flushed"
              id="code-field"
              type="text"
              {...register("code")}
              placeholder="formula/complexity"
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

const postPL2 = async (data: ExerciseFormData) => {
  await postPrivateDataVoid("/api/exercises?exType=PL2", data);
};

export default PL2Form;
