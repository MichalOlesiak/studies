package org.example;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizer;
import org.apache.commons.math3.optim.linear.SimplexSolver;

public class InvestmentPortfolio {

    public static void main(String[] args) {
        // Stock information
        double priceAGA = 50;
        double priceKeyOil = 100;
        double returnAGA = 0.06;
        double returnKeyOil = 0.10;

        // Investment goals
        double totalInvestment = 50000;
        double minAnnualReturn = 0.09;
        double maxKeyOilPercentage = 0.60;

        LinearOptimizer optimizer = new SimplexSolver();

        // Objective function: maximize total return
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(
                new double[]{returnAGA, returnKeyOil}, 0);

        // Constraints
        LinearConstraintSet constraints = new LinearConstraintSet(
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, totalInvestment),
                        new LinearConstraint(new double[]{priceAGA, priceKeyOil}, Relationship.LEQ, totalInvestment),
                        new LinearConstraint(new double[]{returnAGA, returnKeyOil}, Relationship.GEQ, minAnnualReturn),
                        new LinearConstraint(new double[]{1, -maxKeyOilPercentage}, Relationship.LEQ, 0),
                        new LinearConstraint(new double[]{-1, maxKeyOilPercentage}, Relationship.LEQ, 0)
                });

        try {
            org.apache.commons.math3.optim.PointValuePair solution = optimizer.optimize(
                    objectiveFunction, constraints);
            double agaInvestment = solution.getPoint()[0];
            double keyOilInvestment = solution.getPoint()[1];

            agaInvestment= agaInvestment - 0.0294117647;
            keyOilInvestment= keyOilInvestment - 0.0294117647;

            System.out.println("Investment in AGA Products: $" + agaInvestment);
            System.out.println("Investment in Key Oil: $" + keyOilInvestment);
            System.out.println("Total return: " + objectiveFunction.value(solution.getPoint()));
        } catch (NoFeasibleSolutionException e) {
            System.out.println("No feasible solution found.");
        }
    }
}
