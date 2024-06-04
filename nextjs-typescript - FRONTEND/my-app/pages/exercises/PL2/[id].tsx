import { GetStaticPaths } from "next";
import { GradeResponse, PL2, TruthTable } from "../../../types";
import {
  Button,
  Center,
  Heading,
  HStack,
  Input,
  Table,
  TableContainer,
  Tbody,
  Thead,
  Tr,
} from "@chakra-ui/react";
import { useEffect, useState } from "react";
import {
  deletePrivateData,
  getPublicData,
  postPublicData,
} from "../../../utils/NetworkUtils";
import Head from "next/head";
import DefaultContainer from "../../../components/common/DefaultContainer";
import TitleHeading from "../../../components/common/TitleHeading";
import BaseTh from "../../../components/common/table/Th";
import BaseTd from "../../../components/common/table/Td";
import FillableTd from "../../../components/common/table/TdFillable";
import _ from "lodash";
import { useAuth } from "../../../contexts/auth";
import { prettify } from "../../../components/common/Formula";

// INTERFACES

interface SubmitData {
  student: string;
  answer: Pair[];
}

interface Pair {
  first: string;
  second: boolean[];
}

interface PageProps {
  exercise: PL2;
  truthTable: TruthTable;
}

function PL2ExercisePage({ exercise, truthTable }: PageProps) {
  const { user } = useAuth();
  // VARIABLE DECLARATIONS
  const size = truthTable.subformulas.length + 1;
  const [values, setValues] = useState<number[][]>(
    new Array(size)
      .fill(-1)
      .map(() => new Array(truthTable.grid.length).fill(-1))
  );
  const [errors, setErrors] = useState<number[]>(new Array(size).fill(-1));
  const [subformulasInput, setSubformulasInput] = useState<string[]>(
    new Array(size - 1).fill("")
  );

  const [correct, setCorrect] = useState<boolean>(false);

  // FUNCTIONS

  useEffect(() => {
    if (exercise.complexity == 0) {
      setSubformulasInput(truthTable.subformulas);
    }
  }, []);

  async function handleSubmit() {
    try {
      validateTable();

      let map: Map<string, boolean[]> = new Map();

      subformulasInput.map((subformula, index) => {
        let booleans: boolean[] = new Array(values[index].length);
        values[index].map((value, index) => {
          booleans[index] = !!value;
        });
        map.set(subformula, booleans);
      });

      let finalValues: boolean[] = new Array(values[values.length - 1].length);
      values[values.length - 1].map((value, index) => {
        finalValues[index] = !!value;
      });

      map.set(exercise.formula, finalValues);

      // Map to list of pairs TODO: CHANGE FROM MAP
      let listOfPairs: Pair[] = new Array();
      map.forEach((value: boolean[], key: string) => {
        let pair: Pair = {
          first: key,
          second: value,
        };
        listOfPairs.push(pair);
      });

      const data: SubmitData = {
        student: user != null ? user.username : "guest_user",
        answer: listOfPairs,
      };

      console.log(data);
      await postPublicData<GradeResponse>(
        `/api/exercises/${exercise.id}/grades`,
        data
      )
        .then((grade) => {
          console.log(grade);
          if (grade.grade == 100) setCorrect(true);
          const errors = grade.errors;
          const array: number[] = [];
          errors.map((err: boolean, i: number) => {
            if (err) {
              array[i] = 0;
            } else {
              array[i] = 1;
            }
          });
          setErrors([...array]);
        })
        .catch((error) => {
          console.error(error.message);
        });
    } catch (e) {
      if (e instanceof Error) {
        console.log("error: " + e.message);
        window.alert(e.message);
      }
    }
  }

  const handleChange =
    (idx: number) => (e: React.ChangeEvent<HTMLInputElement>) => {
      const array = subformulasInput;
      array[idx] = e.currentTarget.value;
      setSubformulasInput([...array]);
    };

  const checkSubformulas = () => {
    const correctSet = new Set(truthTable.subformulas);
    const submittedSet = new Set(subformulasInput);
    submittedSet.forEach((formula) => {
      formula = formula.trim();
      if (!correctSet.has(formula)) {
        throw Error("Submitted subformulas are incorrect!");
      }
    });
  };

  const validateTable = () => {
    // Check if table is all filled up
    values.map((row) => {
      row.map((value) => {
        if (value == -1) throw Error("You need to fill the entire table!");
      });
    });
    // Check if subformulas are correct if complexity == 1
    if (exercise.complexity == 1) {
      subformulasInput.map((formula) => {
        if (formula == "") throw Error("You need to fill the subformulas row!");
      });
      checkSubformulas();
    }
  };

  const handleClick = (i: number, j: number) => {
    const array = values;
    if (values[j][i] == -1) {
      array[j][i] = 0;
    } else if (values[j][i] == 0) {
      array[j][i] = 1;
    } else if (values[j][i] == 1) {
      array[j][i] = 0;
    }
    setValues([...array]);
  };

  const getBackgroundColor = (value: number) => {
    let color;
    if (value === -1) {
      color = "white";
    } else if (value === 0) {
      color = "#DC143C";
    } else if (value === 1) color = "#228B22";
    return color;
  };


  return (
    <>
      <Head>
        <title>Exercise {exercise.id}</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <DefaultContainer>
        <TitleHeading title={"φ = " + prettify(exercise.formula)} />

        <TableContainer>
          <Table variant="unstyled" border={"2px solid black"} size="md">
            {/*TABLE HEAD*/}
            <Thead background={"gray.300"} border={"1px solid black"}>
              <Tr>
                {/*Table Head - Variables*/}
                {truthTable.variables.map((variable, index) => {
                  return <BaseTh key={index} children={variable} />;
                })}
                {/*Table Head - Subformulas depending on complexity*/}
                {truthTable.subformulas.map((subformula, index) => {
                  if (exercise.complexity === 0) {
                    return <BaseTh key={index} children={subformula} />;
                  } else if (exercise.complexity === 1) {
                    const input = (
                      <Input
                        size={"sm"}
                        onChange={handleChange(index)}
                        value={subformulasInput[index]}
                        width="10vh"
                      />
                    );
                    return <BaseTh key={index} children={input} />;
                  }
                })}
                {/*Table Head - Formula*/}
                <BaseTh children={"φ"} />
              </Tr>
            </Thead>
            {/*TABLE BODY*/}
            <Tbody>
              {truthTable.grid.map((row, i) => (
                <Tr key={i}>
                  {row.map((value, i) => {
                    return <BaseTd key={i} children={value} />;
                  })}
                  {_.times(truthTable.subformulas.length, (j) => {
                    return (
                      <FillableTd
                        key={j}
                        background={getBackgroundColor(errors[j])}
                        onClick={() => handleClick(i, j)}
                        children={
                          values[j][i] == -1
                            ? ""
                            : values[j][i] == 1
                            ? "T"
                            : "F"
                        }
                        isDisabled={correct}
                      />
                    );
                  })}
                  <FillableTd
                    background={getBackgroundColor(
                      errors[truthTable.subformulas.length]
                    )}
                    onClick={() => handleClick(i, values.length - 1)}
                    children={
                      values[values.length - 1][i] == -1
                        ? ""
                        : values[values.length - 1][i] == 1
                        ? "T"
                        : "F"
                    }
                    isDisabled={correct}
                  />
                </Tr>
              ))}
            </Tbody>
          </Table>
        </TableContainer>

        <HStack pt={5}>
          <Center>
            {!correct ? (
              <Button onClick={handleSubmit}>Submit Answer</Button>
            ) : (
              <Heading>Answer is correct!</Heading>
            )}
          </Center>
        </HStack>
      </DefaultContainer>
    </>
  );
}

// REQUESTS TO THE SERVER

async function fetchTruthTable(params: any): Promise<TruthTable> {
  const truthTable: TruthTable = await getPublicData<TruthTable>(
    `/api/exercises/${params.id}/truth-table`
  );
  return truthTable;
}

async function fetchPL2(params: any): Promise<PL2> {
  const exercise: PL2 = await getPublicData<PL2>(`/api/exercises/${params.id}`);
  return exercise;
}

export const getStaticPaths: GetStaticPaths = async () => {
  const exercises: PL2[] = await getPublicData<PL2[]>(
    "/api/exercises?exType=PL2"
  );
  const paths = exercises.map((exercise) => {
    return { params: { id: String(exercise.id) } };
  });

  return {
    paths,
    fallback: false,
  };
};

export async function getStaticProps({ params }: { params: any }) {
  const exercise: PL2 = await fetchPL2(params);
  const truthTable: TruthTable = await fetchTruthTable(params);

  return {
    props: {
      exercise,
      truthTable,
    },
  };
}

export default PL2ExercisePage;
