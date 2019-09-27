//Altta Bulunan fonksiyonu "onCrate" fonksiyonu ile değiştirip denemeler yapabilirsiniz

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Variables
        //Integer

        var x = 5
        val y = 4

        x = 6

        //println(x * y)

        var age = 20

        age = 23

        val result = age * 5 / 4

        //println(result)


        val myInteger : Int = 5


        //Double & Float

        val pi = 3.14

        val r : Double = 5.0

        val myAge = 23.0

        val myResult = myAge * 5 / 4

        //println(myResult)

        //String


        val name = "Ali"
        val surname = "Örnek"

        val fullname = name + " " +surname

        //println(fullname)

        val myName : String = "Ahmet"

        //Boolean

        var isAlive : Boolean = true
        isAlive = false


        //Arrays

        val myArray = arrayOfNulls<String>(4)

        myArray[0] = "Ali"
        myArray[1] = "Ahmet"
        myArray[2] = "Ayse"
        myArray[3] = "Fatma"

        //println(myArray[2])



        val myNumberArray = intArrayOf(10,20,30,40,50)
        //println(myNumberArray.size)
        myNumberArray.set(2,35)
        //println(myNumberArray[2])


        //List


        val ogrenciler = ArrayList<String>()

        ogrenciler.add("Ali")
        ogrenciler.add("Ahmet")

        //println(ogrenciler)

        ogrenciler.add(1,"Ayse")

        ogrenciler.add("Ayse")

        //println(ogrenciler)

        //Set

        val mySet = HashSet<String>()

        mySet.add("Ayse")
        mySet.add("Ayse")

        //println(mySet.size)


        //Map

        val myHashMap = HashMap<String, String>()

        myHashMap.put("name", "Ali")
        myHashMap.put("instrument", "Guitar")

        //println(myHashMap["instrument"])


        //Operators

        var m = 5
        println(m)
        m = m + 1
        //println(m)
        m++
        //println(m)
        m--
        //println(m)

        var n = 6


        //println(m>n)
       // println(n>m)

        // >
        // <
        // >=
        // <=
        // ==
        // !=
        // &&
        // ||

        //if

        if (m > n) {
            //println("m is greater than n")
        } else if ( n > m) {
           // println("n is greater than m")
        } else {
           // println("m = n")
        }


        //switch

        val day = 3
        var dayString = ""

        if (day == 1) {
            dayString = "Monday"
        } else if (day == 2) {
            dayString = "Tuesday"
        } else if (day == 3) {
            dayString = "Wednesday"
        }


        when (day) {

            1 -> dayString = "Monday"
            2 -> dayString = "Tuesday"
            3 -> dayString = "Wednesday"
            else -> dayString = "Sunday"

        }

        //println(dayString)




        //for loops


        val myNumbers = intArrayOf(12,15,18,21,24)

        val q = myNumbers[0] / 3 * 5
        println(q)


        for (number in myNumbers) {
            val z = number / 3 * 5
            println(z)
        }


        for (i in myNumbers.indices) {
            val y = myNumbers[i] / 3 * 5
            println(y)
        }


        for (a in 0..9) {
            val b = a * 10
            println(b)
        }

        //While Loop

        var j = 0

        while (j<10) {
            val x = j * 10
            println(x)
            j++
        }
    }