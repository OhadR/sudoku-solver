var backend_url = '/suduku_solver';
var SOLVE 	= 'Solve!';
var BACK	= 'Back';

function OnLoad()
{
	setButtonToSolve();
}

function setButtonToSolve()
{
	document.getElementById('solve_and_reset').value = SOLVE;
}

function OnSolve()
{
	var buttonText = document.getElementById('solve_and_reset').value;
	if(buttonText == SOLVE)
	{
		var puzzle = getTableAsJson();

		$.ajax({
			url : backend_url,
			type: 'GET',
			data: 
			{
				puzzle: puzzle,
				command: 'solve'
			},
			dataType: "json",
			success: function(response)
			{
				populateResult(response);
			}
		});

		document.getElementById('solve_and_reset').value = BACK;

	}
	else if(buttonText == BACK)
	{
		//clear the puzzle and set again the button the "Solve!":
		var x, y;
		for(x=0; x<9; ++x)
		{
			for(y=0; y<9; ++y)
			{
				var xStr = x+''; 
				var yStr = y+'';
				var indexStr = xStr + yStr;
				var element = document.getElementById('_' + indexStr);
				element.value = '';
			}
		}

		setButtonToSolve();
	}


}


function OnGetHint()
{
	var puzzle = getTableAsJson();

	$.ajax({
		url : backend_url,
		type: 'GET',
		data: 
		{
			puzzle: puzzle,
			command: 'gethint'
		},
		dataType: "json",
		success: function(response)
		{
			populateResult(response);
		}
	});
}


function getTableAsJson()
{
	var puzzle = '';
	var x, y;
	puzzle = puzzle + '[';
	for(x=0; x<9; ++x)
	{
		puzzle = puzzle + '[';
		for(y=0; y<9; ++y)
		{
			var xStr = x+''; 
			var yStr = y+'';
			var indexStr = xStr + yStr;
			var val = document.getElementById('_' + indexStr).value;
			if(val != '')
			{
				puzzle = puzzle + val;
			}
			else
			{
				puzzle = puzzle + '0';
			}
			if(y != 8)
			{
				puzzle = puzzle + ',';
			}
		}
		puzzle = puzzle + ']';
		if(x != 8)
		{
			puzzle = puzzle + ',';
		}
	}
	puzzle = puzzle + ']';

//	alert( puzzle );
	
	return puzzle;
}


function populateResult(response)
{
	$('#status').html( response.message );
	if(response.success)
	{
		var x, y;
		for(x=0; x<9; ++x)
		{
			for(y=0; y<9; ++y)
			{
				if(response.puzzle[x][y] != 0)
				{
					var xStr = x+'';
					var yStr = y+'';
					var indexStr = xStr + yStr;
					var element = document.getElementById('_' + indexStr);
					var val = response.puzzle[x][y];
					element.value = val;
				}
			}
		}
	}
}


//called onKeyPress, to validate the input (even though i protect the server-side, i check the incoming JSON)
function isValidNumber(e)
{
	var key;
	var keychar;

	if (window.event)
	{
		key = window.event.keyCode;
	}
	else if (e)
	{
		//alert(e.which);
		key = e.which;
	}
	else 
	{	
		return true;
	}

	keychar = String.fromCharCode(key);
	if ((key!="")&&((("123456789").indexOf(keychar) > -1)||(key==8)||(key==0)))
	{
		return true;
	}
	else 
	{
		return false;
	}

} 