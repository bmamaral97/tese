import { useRouter } from 'next/router'
import { GetStaticPaths } from 'next'
import { PL1 } from "../../../types";

function CharacterPage({ exercise }: { exercise: PL1 }) {
  return (
    <div>
        <h1>boas</h1>
      <h1>{exercise.id}</h1>
    </div>
  );
}

  
  export const getStaticPaths: GetStaticPaths = async () => {
    
    //const url1 = 'http://localhost:8080/api/exercises/PL1/'
    const url = '/api/exercises/PL1/'

    const res = await fetch(url);
    const exercises: PL1[] = await res.json();
  
    return {
      paths: exercises.map((exercise) => {
        return { params: { id: String(exercise.id) } };
      }),
      fallback: true,
    };
  }


  export async function getStaticProps({ params}: {params:any} ) {
  
    //const url1 = `http://localhost:8080/api/exercises/PL1/${params.id}`
    const url = `/api/exercises/PL1/${params.id}`

    const res = await fetch(url);
    const exercise = await res.json();

    return {
    props: {
        exercise,
    }};
}

export default CharacterPage;