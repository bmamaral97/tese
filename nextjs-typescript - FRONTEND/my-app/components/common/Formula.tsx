import { Text } from "@chakra-ui/react";

interface Props {
  formula: string;
}

export const prettify = (formula: string): string => {
  let prettyFormula: string[] = [];
  let index = 0;
  while (index < formula.length) {
    let currentChar = formula[index];
    switch (currentChar) {
      case " ":
        index++;
        break;
      case "(":
        prettyFormula.push("(");
        index++;
        break;
      case ")":
        prettyFormula.push(")");
        index++;
        break;
      case "&":
        prettyFormula.push("∧");
        index++;
        break;
      case "|":
        prettyFormula.push("∨");
        index++;
        break;
      case "~":
        prettyFormula.push("¬");
        index++;
        break;
      case "=":
        prettyFormula.push("⇒");
        index = index + 2;
        break;
      case "<":
        prettyFormula.push("⇔");
        index = index + 3;
        break;
      default:
        prettyFormula.push(currentChar);
        index++;
        break;
    }
  }
  return prettyFormula.join("");
};

function Formula({ formula }: Props) {
  return <Text>{prettify(formula)}</Text>;
}

export default Formula;
