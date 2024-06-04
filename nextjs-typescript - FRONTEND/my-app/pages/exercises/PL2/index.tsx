import { Button, Link, ListItem, UnorderedList } from "@chakra-ui/react";
import { GetServerSideProps } from "next";
import Head from "next/head";
import Router from "next/router";
import DefaultContainer from "../../../components/common/DefaultContainer";
import { prettify } from "../../../components/common/Formula";
import RoundedBox from "../../../components/common/RoundedBox";
import TitleHeading from "../../../components/common/TitleHeading";
import NoExercisesAvailable from "../../../components/templates/NoExercisesAvailable";
import { useAuth } from "../../../contexts/auth";
import { PL2 } from "../../../types";
import { deletePrivateData, getPublicData } from "../../../utils/NetworkUtils";

export interface PL2PageProps {
  exercises: PL2[];
}

const handleDelete = async (id: number) => {
  await deletePrivateData(`/api/exercises/${id}`).then(() => {
    Router.push("/exercises/PL2");
  });
};

const PL2Page = ({ exercises }: PL2PageProps) => {
  const { isAdmin } = useAuth();

  return (
    <div>
      <Head>
        <title>PL2 Page</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <DefaultContainer>
        <TitleHeading title="PL2 Page" />
        {Array.isArray(exercises) && exercises.length !== 0 ? (
          <RoundedBox>
            <UnorderedList styleType="none" spacing={2}>
              {exercises.map((exercise, index) => {
                return (
                  <ListItem key={index}>
                    <Link href={"/exercises/PL2/" + exercise.id}>
                      Exercise: {prettify(exercise.formula)} / Complexity:{" "}
                      {exercise.complexity}
                    </Link>
                    {isAdmin && (
                      <Button
                        size={"xs"}
                        ml={2}
                        onClick={() => handleDelete(exercise.id)}
                      >
                        X
                      </Button>
                    )}
                  </ListItem>
                );
              })}
            </UnorderedList>
          </RoundedBox>
        ) : (
          <NoExercisesAvailable />
        )}
      </DefaultContainer>
    </div>
  );
};

export const getServerSideProps: GetServerSideProps = async () => {
  const exercises: PL2[] = await getPublicData<PL2[]>(
    "/api/exercises?exType=PL2"
  );
  return {
    props: { exercises: exercises },
  };
};

export default PL2Page;