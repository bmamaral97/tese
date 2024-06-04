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

function PL1Form({ user }: ExerciseFormProps) {
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
    await postPL1(exercise);
    alert("Posted new PL1 Exercise");
    reset();
    Router.replace(Router.asPath);
  };

  return (
    <Box>
      <Text>Enter a PL1 code:</Text>
      <form autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
        <FormControl isInvalid={!!errors?.code?.message} isRequired>
          <InputGroup>
            <Input
              variant="flushed"
              id="code-field"
              type="text"
              {...register("code")}
              placeholder="scenario/phrase/formula"
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

const postPL1 = async (data: PostExercise) => {
  await postPrivateDataVoid("/api/exercises?exType=PL1", data);
};

export default PL1Form;
