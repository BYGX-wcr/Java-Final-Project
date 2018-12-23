package environment;

import creature.*;

public class SortController {
    //Sort Calabash Brothers by Quick-sort Algorithm based on color
    public static void sort(CalabashBrother[] huluwa){
        quickSort(huluwa, 0, huluwa.length - 1);

        for (CalabashBrother it : huluwa) {
            it.printCol();
        }
        System.out.print('\n');
    }

    //Execute a recursion process
    private static void quickSort(CalabashBrother[] huluwa, int start, int end) {
        if (start >= end) return;

        int pivot = start;
        int mid = start;
        for (int i = start + 1; i <= end; ++i) {
            if (huluwa[i].compareCol(huluwa[pivot]) < 0) {
                mid++;
                if (mid == i) continue;

                CalabashBrother obj1 = huluwa[i];
                CalabashBrother obj2 = huluwa[mid];
                obj1.displayMov(i, mid);
                obj2.displayMov(mid, i);
                obj1.moveTo(huluwa, mid);
                obj2.moveTo(huluwa, i);
            }
        }

        if (mid != pivot) {
            CalabashBrother obj1 = huluwa[pivot];
            CalabashBrother obj2 = huluwa[mid];
            obj1.displayMov(pivot, mid);
            obj2.displayMov(mid, pivot);
            obj1.moveTo(huluwa, mid);
            obj2.moveTo(huluwa, pivot);
        }

        quickSort(huluwa, start, mid - 1);
        quickSort(huluwa, mid + 1, end);
    }
}
