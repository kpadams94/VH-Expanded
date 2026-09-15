param([int]$Variant=1)
Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
$mask=[Drawing.Bitmap]::new((Join-Path $root "native/0$Variant.png"));$im=[Drawing.Bitmap]::new(16,16)
# Outline only exterior-adjacent pixels; the narrow openings stay dark.
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){if($mask.GetPixel($x,$y).A -eq 0){continue};foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){$xx=$x+$d[0];$yy=$y+$d[1];if($xx -ge 0 -and $xx -lt 16 -and $yy -ge 0 -and $yy -lt 16){$im.SetPixel($xx,$yy,[Drawing.ColorTranslator]::FromHtml('#4B2519'))}}}}
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){if($mask.GetPixel($x,$y).A -eq 0){continue};$hex='#FFCF54';if($x -eq 7){$hex='#FFF2BC'}elseif($y -in @(3,7,11)){$hex='#D86B27'}elseif($y -in @(5,9,14)){$hex='#FFDF79'}elseif($x -lt 7){$hex='#F5A53B'}else{$hex='#EB8B32'};$im.SetPixel($x,$y,[Drawing.ColorTranslator]::FromHtml($hex))}}
$im.Save((Join-Path $root 'native/impact-draft.png'));$mask.Dispose();$im.Dispose()
