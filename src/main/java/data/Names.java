package data;

public class Names {
    public static String gateSearchName = "Gate";
    public static String gateNotName = "Gate Not";
    public static String gateAnd2Name = "Gate And 2";
    public static String gateAnd3Name = "Gate And 3";
    public static String gateAnd4Name = "Gate And 4";
    public static String gateOr2Name = "Gate Or 2";
    public static String gateOr3Name = "Gate Or 3";
    public static String gateOr4Name = "Gate Or 4";
    public static String gateXor2Name = "Gate Xor 2";
    public static String gateXor3Name = "Gate Xor 3";
    public static String gateXor4Name = "Gate Xor 4";
    public static String gateNand2Name = "Gate Nand 2";
    public static String gateNand3Name = "Gate Nand 3";
    public static String gateNand4Name = "Gate Nand 4";
    public static String gateNor2Name = "Gate Nor 2";
    public static String gateNor3Name = "Gate Nor 3";
    public static String gateNor4Name = "Gate Nor 4";
    public static String gateXnor2Name = "Gate Xnor 2";
    public static String gateXnor3Name = "Gate Xnor 3";
    public static String gateXnor4Name = "Gate Xnor 4";
    public static String lineName = "Line";
    public static String switchSearchName = "Switch";
    public static String switchMonostableName = "Switch monostable";
    public static String switchBistableName = "Switch bistable";
    public static String switchPulseName = "Switch pulse";
    public static String flipFlopSearchName = "Flip-flop";
    public static String flipFlopD = "Flip-flop D";
    public static String flipFlopT = "Flip-flop T";
    public static String flipFlopJK = "Flip-flop JK";
    public static String bulbName = "Bulb";
    public static String connectorName = "Connector";
    public static String pointOutputName = "Output";
    public static String pointCenterName = "Center";
    public static String pointInputName = "Input";
    public static String aboutAuthorTitle = "O autorze";
    public static String aboutAuthorHeader = "Praca inżynierska o wdzięcznej nazwie: Symulator układów cyfrowych";
    public static String aboutAuthorContent = "Kamil Kluba 226016\nv1.0";
    public static String unsavedChangesTitle = "Niezapisane zmiany";
    public static String unsavedChangesHeader = "Posiadasz niezapisane zmiany. Czy na pewno chcesz wyłączyć?";
    public static String manualTitle = "Instrukcja obsługi";
    public static String manualCreatingComponentsHeader = "Tworzenie komponentów";
    public static String manualCreatingComponentsContent = "Aby stworzyć komponent najpierw wybierz go w tabeli komponentów" +
            " klikając na dany komponent LPM, lub przyciśnij któryś z klawiszy numerycznych, aby wybrać komponent o wartości" +
            " wciśniętego klawisza (10 dla klawisza 0). Następnie umieść komponent gdzieś na schemacie. Gdy umieścisz go na już" +
            " istniejącym komponencie komponent nie zostanie umieszczony, a wokół wszystkich komponentów pojawią się kwadraty" +
            " sygnalizujące gdzie możesz je umieszczać (po umiejscowieniu możesz nasuwać komponenty na siebie, ale nie zaleca się" +
            " tego robić ze względu na przejrzystość schematu. Dlatego to zabezpieczenie działa tylko przy tworzeniu).\n" +
            "Utworzony komponent (o ile nie jest łącznikiem lub linią) dostanie swój identyfikator dla danej grupy komponentów," +
            " a na wykresie przebiegów czasowych pojawi się nowa seria utworzona specjalnie dla niego.\nPodczas tworzenia" +
            " komponentu możesz także przytrzymać klawisz Ctrl co sprawi, że komponent zostanie wyrównany do siatki.";
    public static String manualComponentsHeader = "Komponenty";
    public static String manualComponentsContent = "Aplikacja pozwala na budowę układów cyfrowych z podstawowych komponentów" +
            " używanych na kursie Logiki Układów Cyfrowych. Wszystkie komponenty (nie licząc liń, które opiszę w osobnym podpunkcie)" +
            " można: tworzyć, usuwać, zaznaczać oraz przesuwać. Stan komponentu opisany jest kolorem, a także zaznaczony jest na przebigu czasowym" +
            " (obie funkcje opisane niżej). Akcje takie jak usuwanie, przesuwanie grupy komponentów, obracanie są możliwe tylko gdy" +
            " dany komponent jest zaznaczony. Można to zrobić poprzez kliknięcie na komponencie, lub zaznaczenie go w granatowe okienko" +
            " przeciagając po ekranie przyciśnięty przycisk myszy (jak zaznaczanie ikonek w systemach operacyjnych). Do każdego" +
            " wejścia/wyjścia komponentu można przyłączyć dowolną liczbę liń.";
    public static String manualGatesHeader = "Bramki";
    public static String manualGatesContent = "Jak bramki dzialają, każdy wie - nie trzeba tłumaczyć, więc:\n" +
            "Not - sygnał wysoki na wyjściu, gdy na wejściu jest niski i odwrotnie.\n" +
            "And - sygnał wysoki na wyjściu, gdy wszystie wejścia mają stan wysoki.\n" +
            "Or - sygnał wysoki na wyjściu, gdy którekolwiek z wejść ma stan wysoki.\n" +
            "Xor - sygnał wysoki na wyjściu, gdy nieparzysta liczba wejść ma stan wysoki.\n" +
            "Nand - sygnał niski na wyjściu, gdy wszystkie wejśćia mają stan wysoki.\n" +
            "Nor - sygnał niski na wyściu, gdy którekolwiek z wejść ma stan wysoki.\n" +
            "Xnor - sygnał niski na wyjściu, gdy nieparzysta liczba wejść ma stan wysoki.\n" +
            "Bramki, których nazwa zaczyna się na N, jako stan bazowy wysyłąją sygnał wysoki, pozostałe wysyłają stan niski." +
            "Program umożliwia obracanie bramek poprzez naciśnięcie klawisza R lub przycisku \"Obróć\", powoduje to obrócenie bramkio 90 stopni.";
    public static String manualSwitchesHeader = "Przełączniki";
    public static String manualSwitchesContent = "Aplikacja pozwala na utworzenie trzech rodzajów przełączników: monostabilnego," +
            "bistabilnego, oraz pulsacyjnego. Każdy z nich jest uruchamiany/wyłączany prawym przyciskiem myszy.\n" +
            "Przełącznik monostabilny - wysyła sygnał wysoki tak długo, jak długo przyciskamy na nim PPM.\n" +
            "Przełącznik bistabilny - wysyła dany sygnał do czasu, aż nie zostanie zmieniony jego stan.\n" +
            "Przełącznik pulsacyjny - gdy jest włączony, to jego stan zmienia się określony kwant czasu. Gdy jest wyłączony" +
            " wysyła sygnał niski. Obrócenie przełącznika poprzez naciśnięcie klawisza R lub przycisku \"Obróć\" powoduje zmianę" +
            " położenia punktu, do którego przyczepiane są linie.";
    public static String manualFlipFlopsHeader = "Przerzutniki";
    public static String manualFlipFlopsContent = "Aplikacja pozwala na utworzenie trzech rodzajów przerzutników: D, T, oraz JK." +
            "Przerzutnik zmienia swój stan w momencie, gdy na wejściu \"Clock\" pojawi się styan wysoki. Stan ten zależy od" +
            "dotychczasowego stanu przerzutnika, oraz wartości sygnału na wejściu \"Input\".\n" +
            "Przerzutnik D - osiąga stan 0, gdy \"Input\" ma wartość 0, analogicznie dla 1.\n" +
            "Przerzutnik T - zmiana stanu z 0 na 1, oraz z 1 na 0 odbywa się, gdy na wejściu \"Input\" jest stan wysoki." +
            " Gdy jest tam stan niski, stan przerzutnika nie zmienia się.\n" +
            "Przerzutnik JK - tutaj stan wysoki ustawiany jest stanem wysokim wejścia \"J\", natomiast stan niski, stanem wysokim" +
            " wejścia \"K\". Gdy obydwa wejścia mają stan niski, stan całego przerzutnika nie zmienia się, natomiast gdy obydwa" +
            " mają stan wysoki, stan przerzutnika zmienia się z 0 na 1, a następnie z 1 na 0 z kolejnymi cyklami zegara.";
    public static String manualBulbsHeader = "Żarówki";
    public static String manualBulbsContent = "Apliakcja pozwala na tworzenie żarówek, których jedynym zadaniem jest sygnalizowanie" +
            " wartości sygnału na danej linii. Obrócenie żarówki poprzez naciśnięcie klawisza R lub przycisku \"Obróć\" powoduje" +
            " jej obrót o 90 stopni.";
    public static String manualConnectorsHeader = "Łączniki";
    public static String manualConnectorsContent = "Łączniki są komponentami, których zadaniem jest utrzymywanie porządku na schemacie.\n" +
            "Jako, że linia może być prowadzona tylko do dwóch komponentów, są one czymś w rodzaju rozszerzeń dla liń.\n" +
            "Do przełącznika może być połączone dowolnie dużo liń, co wraz z jego wyglądem sprawia, że linia wygląda jakby była" +
            " podłączona do wielu komponentów. Aby zaznaczyć łącznik kliknij na niego, lub kliknij dowolny punkt w niedużym promieniu" +
            " od środka łącznika.";
    public static String manualLinesHeader = "Linie";
    public static String manualLinesContent = "Linia jest elementem, której zadaniem jest łączenie ze sobą dwóch komponentów, oraz" +
            " przesyłanie między nimi sygnału, jeśli wejście danego komponentu do którego jest ona podłączona wysyła sygnał.\n" +
            "Linia może zostać \"złamana\" poprzez przyciśnięcie LPM gdzieś na linii i przesunięcie myszy. Pozwala to na utrzymanie porządku " +
            "na schemacie.\nW momencie, gdy chcesz usunąć złamanie na linii użyj kombnacji Ctrl + Z (zaraz po utworzeniu złamania), lub" +
            " klikij na nie, a następnie przytrzymując klawisz Shift wciśnij Delete (bez wciśniętego klawisza Shift cała linia zostanie usunięta.\n" +
            "Złamania na linii mogą być wyrównane do siatki, gdy podczas przenoszenia lub tworzenia ich przytrzymasz klawisz Ctrl.";
    public static String manualWorkspaceHeader = "Pole robocze";
    public static String manualWorkspaceContent = "Pole robocze jest określonych rozmiarów. Na słabszych komputerach, może powodować to" +
            " spowolnioną pracę programu. Aby temu zapobiec, wielkość pola roboczego może zostać zmieniona. Wystarczy wejść w menu Edycja >" +
            " Zmień rozmiar pola roboczego. Po zmianie wielkości pola, komponenty zostaną przesunięte w lewy góry róg.\n" +
            "Pole robocze jest zoomowane scrollem myszki, a takrze przesuwane, gdy przytrzyma się PPM, co bardzo ułatwia na nim pracę.";
    public static String manualTimeCoursesHeader = "Przebiegi czasowe";
    public static String manualTimeCoursesContent = "Aplikacja dostarcza system monitorowania stanów komponentów na przebiegach czasowych.\n" +
            "Każda zmiana stanu komponentu, zostanie odnotowana na wykresie. Niestety rozwiązanie to, ze względu na swoją drugorzędność, oraz" +
            " brak czasu na implementację posiada kilka wad. Są to między innymi ogromne obciążenie komputera dla układów, w których cały czas" +
            " zmienia się stan jakiegoś komponentu (np. przełącznik pulsacyjny), niemożliwe jest dokładne przybliżenie, aby sprawdzić" +
            " co się działo np. między 150 a 200 milisekundą działania programu, wartości sygnałów czasem wariują przy szybkich zmianach." +
            " Mimo wszystko uważam, że jest to przydatne narzędzie.\n Wykres może być zatrzymywany, wznawiany, resetowany, poszerzany, oraz" +
            " zwężany przy pomocy przycisków, a także przy pomocy scrolla myszki oraz klawiszy Ctrl oraz Shift, osie wykresu są zoomowane" +
            " kolejno w osiach X i Y. Zaleca się, aby przed badaniem wymaganej sekwencji układu, zresetować wykres, ponieważ dla długich" +
            " przebiegów jest on bardzo nieczytelny.";
    public static String manualComponentsFilterHeader = "Filtr komponentów";
    public static String manualComponentsFilterContent = "Aplikacja dostarcza funkcjonalność filtrowania komponentów. Aby to zrobić wystarczy" +
            " w polu tekstowym wpisać dowolną frazę z kolumny z nazwą komponentu, wtedy zostanie on wyświetlony (np. dla Switch bistable" +
            " wpisać bist). Aby wyzerować wartość pola, a jednocześnie skasować filtr, wystarczy kliknąć w obrębie pola LPM.";
    public static String manualRevertingChangesHeader = "Cofanie/powtarzanie zmian";
    public static String manualRevertingChangesContent = "Aplikacja wprowadza system cofania/powtarzania zmian przy pomocy skrótów klawiszowych" +
            " Ctrl + Z, oraz Ctrl + Y. Rozpatrzmy hipotetyczną sytuację: tworzysz 10 akcji, a następnie cofasz 5 z nich. W tym momencie masz" +
            " możliwość powrotu do nich poprzez użycie Ctrl + Y, ale gdy utworzysz nowy komponent, to sekwencja akcji zostaje zapomniana, a" +
            " w miejsce powtórzeń tworzona jest nowa akcja. Dalej jednak możesz wrócić do 5-ciu pierwszych akcji.\n" +
            "Jako akcja mam na myśli tworzenie komponentów, usuwanie komponentów, przesuwanie komponentów, zmiana stanu (tylko przełączników!)," +
            " a także tworzenie, usuwanie i przesuwanie złamań na liniach.";
    public static String manualSavingCircuitHeader = "Zapisywanie/wczytywanie układu";
    public static String manualSavingCircuitContent = "Aplikacja pozwala na zapisywanie i wczytywanie schematu do/z pliku.\n" +
            "Aby zapisać schemat, wybierz menu Plik > Zapisz (by zapisać do pliku z którego został schemat wczytany, nadpisać już istniejący plik" +
            " lub Zapisz jako, aby otworzyć okno wyboru pliku, lub użyj Ctrl + S.\nAby załadować schemat, możesz przycisnać przycisk załaduj z" +
            " pliku na ekranie startowym, wybrać menu Plik > Wczytaj, lub użyć kombinacji Ctrl + O. Zostanie wtedy pokazane okno wyboru pliku.";
    public static String manualKeyboardActionsHeader = "Klawiatura";
    public static String manualKeyboardActionsContent = "W tym podpunkcie wypiszę wszyskie akcje związane z klawiaturą:\n" +
            "Klawisze numeryczne 1-0 - działają jak przyciśnięcie na tabeli komponentów, wybierając kolejny element odpowiadajacy" +
            " wartości klawisza (10 dla klawisza 0), przyspieszając tworzenie schematu.\n" +
            "Ctrl + S - zapisanie schematu.\n" +
            "Ctrl + O - wczytanie schematu.\n" +
            "Ctrl + Z - cofnięcie zmian.\n" +
            "Ctrl + Y - powtórzenie zmian.\n" +
            "Ctrl + A - zaznaczenie wszystkich komponentów.\n" +
            "Esc - odznaczenie wszystkich komponentów.\n" +
            "Ctrl - przy tworzeniu/przesuwaniu komponentów powoduje wyrównanie do siatki, przy scrollowaniu przebiegów czasowych" +
            " powoduje skalowanie osi X.\n" +
            "Alt - przy scrollowaniu przebiegów czasowuch powoduje skalowanie osi Y.\n" +
            "Delete - usuwanie zaznaczonych komponentów.\n" +
            "R - powoduje obrót komponentu.";
    public static String manualMouseActionsHeader = "Mysz";
    public static String manualMouseActionsContent = "Tutaj chyba nie ma co się zbytnio rozpisywac:\n" +
            "LPM - tworzenie, zaznaczanie, przesuwanie komponentów, przesuwanie po przebiegach czasowych, oraz wszystko" +
            " co nie zostało tutaj zapisane.\n" +
            "PPM - przesuwanie po polu roboczym, przełączanie przełączników.\n" +
            "Scroll - skalowanie pola roboczego, przebiegów czasowych (z klawiszami Ctrl i Alt).";
    public static String manualErrorsHeader = "Zgłaszanie błędów";
    public static String manualErrorsContent = "Znalazłeś błąd? Napisz do mnie na:\n226016@student.pwr.edu.pl\ntytułując maila" +
            " Symulator Układów Cyfrowych - błąd, a w treści opisz mi co to za błąd i jak do niego doprowadzić.";
}
