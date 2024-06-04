import { User } from "./contexts/auth";

export interface Scenario {
  id: number;
  name: string;
  creator: string;
  map: Map<string, string>;
  exercises: [PL1];
}

export interface GetScenarios {
  results: [Scenario];
}

export interface PL1 {
  id: number;
  type: string;
  creator: string;
  formula: string;
  phrase: string;
  scenarioId: number;
}

export interface PL2 {
  id: number;
  type: string;
  creator: string;
  formula: string;
  complexity: number;
}

export interface PL3 {
  id: number;
  formula: string;
  normalForm: string;
  endFormula: string;
  complexity: number;
}

export interface PL4 {}

export interface TruthTable {
  variables: string[];
  grid: string[][];
  subformulas: string[];
  values: Map<string, boolean[]>;
}

export interface ExerciseFormData {
  code: string;
}

export interface FileFormData {
  fileName: string;
  file: File | null;
}

export interface PostExercise {
  code: string;
  instructor: string;
}

export interface ExerciseFormProps {
  user: User;
}

export interface GradeResponse {
  grade: number;
  errors: boolean[];
  feedback: string;
}
